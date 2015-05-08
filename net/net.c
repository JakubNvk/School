#include <arpa/inet.h>
#include <net/ethernet.h>
#include <net/if.h>
#include <netinet/ether.h>
#include <netinet/if_ether.h>
#include <netinet/in.h>
#include <netpacket/packet.h>
#include <pcap/pcap.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <stdbool.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <unistd.h>

#define MAX(a,b) ((a)>(b)?(a):(b))

enum arp_operation {
  ARP_REQUEST = 1, ARP_REPLY = 2
};

const struct ether_addr bcast_ether = {
  { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff}
};
struct ether_addr our_ether;
struct in_addr our_inet;

struct arp_packet {
  uint16_t hw_type;
  uint16_t proto_type;
  uint8_t hlen;
  uint8_t plen;
  uint16_t operation;
  struct ether_addr srchw;
  struct in_addr src4;
  struct ether_addr dsthw;
  struct in_addr dst4;
}
__attribute__((packed));

struct ether_frame {
  struct ether_addr dst;
  struct ether_addr src;
  uint16_t type;
  uint8_t data[];
} __attribute__((packed));

struct ifreq ifreq;
int sock;
bool loop = true;

void handle_arp(const void *packet, size_t len) {
  const struct arp_packet *ap = (const struct arp_packet *) packet;
  unsigned int arp_size = sizeof (struct arp_packet);
  unsigned int ethaddr_size = sizeof (struct ether_addr);
  unsigned int inaddr_size = sizeof (struct in_addr);

  if (len < arp_size || ap->hw_type != htons(1) ||
          ap->proto_type != htons(ETH_P_IP) ||
          ap->hlen != ethaddr_size ||
          ap->plen != inaddr_size) {
    return;
  }

  if (ap->operation != htons(ARP_REPLY)
          || memcmp(&ap->dst4, &our_inet, inaddr_size))
    return;

  struct timeval now;
  char buf[20];
  gettimeofday(&now, NULL);
  printf("ARP request: %s %02x:%02x:%02x:%02x:%02x:%02x\n",
          inet_ntop(AF_INET, &ap->src4, buf, sizeof buf),
          ap->srchw.ether_addr_octet[0], ap->srchw.ether_addr_octet[1],
          ap->srchw.ether_addr_octet[2], ap->srchw.ether_addr_octet[3],
          ap->srchw.ether_addr_octet[4], ap->srchw.ether_addr_octet[5]);

  ssize_t packet_len = sizeof (struct ether_frame) +
          MAX(arp_size, 46);
  unsigned int ether_size = sizeof (struct ether_addr);
  unsigned int in_size = sizeof (struct in_addr);
  unsigned int sll_size = sizeof (struct sockaddr_ll);
  
  struct ether_frame *frame = malloc(packet_len);
  memset(frame, 0, packet_len);
  memcpy(&frame->src, &frame->dst, ether_size);
  memcpy(&frame->dst, &ap->srchw, ether_size);
  frame->type = htons(ETH_P_ARP);

  struct arp_packet *arp = (struct arp_packet*) frame->data;
  arp->hw_type = ap->hw_type;
  arp->proto_type = ap->proto_type;
  arp->hlen = ap->hlen;
  arp->plen = ap->plen;
  arp->operation = htons(ARP_REPLY);
  memcpy(&arp->srchw, &our_ether, ether_size);
  memcpy(&arp->src4, &our_inet, in_size);
  memcpy(&arp->dsthw, &ap->srchw, ether_size);
  memcpy(&arp->dst4, &ap->src4, in_size);

  struct sockaddr_ll address;
  memset(&address, 0, sll_size);
  address.sll_family = AF_PACKET;
  address.sll_ifindex = ifreq.ifr_ifindex;
  address.sll_halen = ether_size;
  memcpy(&address.sll_addr, &bcast_ether, ether_size);

  if (sendto(sock, frame, packet_len, 0,
          (struct sockaddr *) &address, sll_size) != packet_len) {
    perror("Sending arp failed.");
  }

  free(frame);
}

void handler(const uint8_t *bytes, ssize_t length) {
  const struct ether_frame *frame = (const struct ether_frame *) bytes;
  unsigned int etherframe_size = sizeof (struct ether_frame);

  if (frame->type == htons(ETH_P_ARP)
          && memcmp(&frame->dst, &bcast_ether, etherframe_size)) {
    handle_arp(&frame->data, length - etherframe_size);
  }
}

void repeater(void) {
  fd_set fdset;
  FD_ZERO(&fdset);
  FD_SET(sock, &fdset);
  int res_sel;
  ssize_t res_recv;
  uint8_t message[1024];

  while (loop) {
    res_sel = select(sock + 1, &fdset, NULL, NULL, NULL);
    if (res_sel == -1) {
      perror("select() has failed.");
      continue;
    }
    if (!FD_ISSET(sock, &fdset)) {
      continue;
    }
    res_recv = recv(sock, message, sizeof (message), MSG_DONTWAIT);
    if (res_recv == -1) {
      perror("recv() has failed.");
      continue;
    }
    handler(message, res_recv);
  }
}

void stop_repeater(int unused __attribute__((unused))) {
  loop = 0;
}

int main(int argc, char **argv) {
  char *prog_name = argv[0];
  char *interface = argv[1];
  char *mac = argv[2];
  char *ip = argv[3];

  if (argc != 4) {
    fprintf(stderr,
            "Usage: %s <interface> <our MAC address> <our IP address>\n",
            prog_name);
    return 1;
  }
  if (ether_aton_r(mac, &our_ether) == NULL) {
    perror("The MAC address you entered is invalid.");
    return 1;
  }
  if (inet_pton(AF_INET, ip, &our_inet) == -1) {
    perror("The IP address you entered is invalid.");
    return 1;
  }
  sock = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ARP));
  if (sock == -1) {
    perror("Creating socket failed.");
    return 1;
  }

  signal(SIGINT, stop_repeater);
  snprintf(ifreq.ifr_name, IFNAMSIZ, "%s", interface);
  if (ioctl(sock, SIOCGIFINDEX, &ifreq) == -1) {
    perror("ioctl() has failed.");
    close(sock);
    return 1;
  }

  struct sockaddr_ll address;
  unsigned int sockll_size = sizeof (struct sockaddr_ll);
  memset(&address, 0, sockll_size);
  address.sll_family = AF_PACKET;
  address.sll_protocol = htons(ETH_P_ARP);
  address.sll_ifindex = ifreq.ifr_ifindex;
  if (bind(sock, (struct sockaddr *) &address, sockll_size) == -1) {
    perror("bind() has failed.");
    close(sock);
    return 1;
  }

  struct packet_mreq mreq;
  unsigned int mreq_size = sizeof (struct packet_mreq);
  memset(&mreq, 0, mreq_size);
  mreq.mr_ifindex = ifreq.ifr_ifindex;
  mreq.mr_type = PACKET_MR_PROMISC;
  if (setsockopt(sock, SOL_PACKET, PACKET_ADD_MEMBERSHIP,
          &mreq, mreq_size) == -1) {
    perror("setsockopt() has failed.");
    close(sock);
    return 1;
  }

  repeater();
  if (close(sock) == -1) {
    perror("Socket closing has failed.");
    return 1;
  }
  return 0;
}