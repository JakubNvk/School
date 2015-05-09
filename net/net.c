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

enum echo_operation {
  ECHO_REQUEST = 8, ECHO_REPLY = 0
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

struct ip_header {
  uint8_t version_ihl;
  uint8_t dscp_ecn;
  uint16_t len;
  uint16_t ident;
  uint16_t frag_flags;
  uint8_t ttl;
  uint8_t proto;
  uint16_t chksum;
  struct in_addr src_addr;
  struct in_addr dst_addr;
} __attribute__((packed));

struct icmp_header {
  uint8_t type;
  uint8_t code;
  uint16_t chksum;
  uint32_t rest;
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

  if (ap->operation != htons(ARP_REQUEST)
          || memcmp(&ap->dst4, &our_inet, inaddr_size)) {
    return;
  }

  ssize_t packet_len = sizeof (struct ether_frame) +
          MAX(arp_size, 46);
  unsigned int ether_size = sizeof (struct ether_addr);
  unsigned int in_size = sizeof (struct in_addr);
  unsigned int sll_size = sizeof (struct sockaddr_ll);

  struct ether_frame *eframe = malloc(packet_len);
  memset(eframe, 0, packet_len);
  memcpy(&eframe->src, &eframe->dst, ether_size);
  memcpy(&eframe->dst, &ap->srchw, ether_size);
  eframe->type = htons(ETH_P_ARP);

  struct arp_packet *ap_reply = (struct arp_packet*) eframe->data;
  ap_reply->hw_type = ap->hw_type;
  ap_reply->proto_type = ap->proto_type;
  ap_reply->hlen = ap->hlen;
  ap_reply->plen = ap->plen;
  ap_reply->operation = htons(ARP_REPLY);
  memcpy(&ap_reply->srchw, &our_ether, ether_size);
  memcpy(&ap_reply->src4, &our_inet, in_size);
  memcpy(&ap_reply->dsthw, &ap->srchw, ether_size);
  memcpy(&ap_reply->dst4, &ap->src4, in_size);

  struct sockaddr_ll address;
  memset(&address, 0, sll_size);
  address.sll_family = AF_PACKET;
  address.sll_ifindex = ifreq.ifr_ifindex;
  address.sll_halen = ether_size;
  memcpy(&address.sll_addr, &ap->srchw, ether_size);

  if (sendto(sock, eframe, packet_len, 0,
          (struct sockaddr *) &address, sll_size) != packet_len) {
    perror("Sending ARP failed.");
  }

  free(eframe);
}

uint16_t checksum(uint16_t *buffer, size_t length) {
  uint32_t chsum = 0;

  while (length > 1) {
    chsum += *buffer;
    buffer++;
    length -= sizeof (uint16_t);
  }
  if (length != 0) {
    chsum += *(uint8_t *) buffer;
  }
  while (chsum >> 16) {
    chsum = (chsum >> 16) + (chsum & 0xffff);
  }

  return ~((uint16_t) chsum);
}

void handle_ip(const void *packet, size_t length, struct ether_addr hw_dst) {
  const struct ip_header *ip = (const struct ip_header *) packet;
  unsigned int iphead_size = sizeof (struct ip_header);
  unsigned int ethframe_size = sizeof (struct ether_frame);
  unsigned int ethaddr_size = sizeof (struct ether_addr);
  unsigned int inaddr_size = sizeof (struct in_addr);

  if (memcmp(&ip->dst_addr, &our_inet, inaddr_size)) {
    return;
  }
  if (checksum((uint16_t *) ip, iphead_size)) {
    return;
  }
  if (ip->proto != IPPROTO_ICMP) {
    return;
  }

  const uint8_t *icmp_data = (const uint8_t *) (packet + iphead_size);
  const struct icmp_header *icmp = (const struct icmp_header *) icmp_data;
  unsigned int sll_size = sizeof (struct sockaddr_ll);
  size_t icmp_length = length - iphead_size;

  if (checksum((uint16_t *) icmp, icmp_length)) {
    return;
  }
  if (icmp->type != ECHO_REQUEST || icmp->code != 0) {
    return;
  }

  ssize_t slength = ethframe_size + MAX(iphead_size + icmp_length, 46);
  struct ether_frame *eframe = malloc(slength);
  memset(eframe, 0, slength);
  memcpy(&eframe->src, &our_ether, ethaddr_size);
  memcpy(&eframe->dst, &hw_dst, ethaddr_size);
  eframe->type = htons(ETH_P_IP);

  struct ip_header *ip_reply = (struct ip_header *) eframe->data;
  memcpy(ip_reply, ip, iphead_size);
  memcpy(&ip_reply->src_addr, &our_inet, inaddr_size);
  memcpy(&ip_reply->dst_addr, &ip->src_addr, inaddr_size);
  ip_reply->chksum = 0;
  ip_reply->chksum = checksum((uint16_t *) ip_reply, iphead_size);

  uint8_t *icmpreply_data = (uint8_t *) (eframe->data + iphead_size);
  memcpy(icmpreply_data, icmp_data, icmp_length);
  struct icmp_header *icmp_reply = (struct icmp_header *) icmpreply_data;
  icmp_reply->type = ECHO_REPLY;
  icmp_reply->code = 0;
  icmp_reply->chksum = 0;
  icmp_reply->chksum = checksum((uint16_t *) icmpreply_data, icmp_length);

  struct sockaddr_ll address;
  memset(&address, 0, sll_size);
  address.sll_family = AF_PACKET;
  address.sll_ifindex = ifreq.ifr_ifindex;
  address.sll_halen = ethaddr_size;
  memcpy(&address.sll_addr, &hw_dst, ethaddr_size);

  if (sendto(sock, eframe, slength, 0, (struct sockaddr *) &address, sll_size)
          != slength) {
    perror("Sending ICMP failed.");
  }

  free(eframe);
}

void handler(const uint8_t *bytes, ssize_t length) {
  const struct ether_frame *eframe = (const struct ether_frame *) bytes;
  unsigned int etherframe_size = sizeof (struct ether_frame);

  if (eframe->type == htons(ETH_P_ARP)
          && memcmp(&eframe->dst, &bcast_ether, etherframe_size)) {
    handle_arp(&eframe->data, length - etherframe_size);
  }

  if (eframe->type == htons(ETH_P_IP)
          && memcmp(&eframe->dst, &our_ether, etherframe_size)) {
    handle_ip(&eframe->data, length - etherframe_size, eframe->src);
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
  sock = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
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
  address.sll_protocol = htons(ETH_P_ALL);
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