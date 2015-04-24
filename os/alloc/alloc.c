#include "wrapper.h"
#include <stdio.h>

struct __attribute__((__packed__)) header {
  unsigned char flag;
  unsigned int size;
};

typedef struct header header_t;

void writeBuffer(unsigned int address, unsigned int length,
        unsigned char *buffer) {
  unsigned int i;
  for (i = 0; i < length; i++) {
    mwrite(address + i, buffer[i]);
  }
}

void readBuffer(unsigned int address, unsigned int length,
        unsigned char *buffer) {
  unsigned int i;
  for (i = 0; i < length; i++) {
    buffer[i] = mread(address + i);
  }
}

header_t getHeader(unsigned int address) {
  int header_size = sizeof (header_t);
  int header_address = address - header_size;
  header_t header;
  readBuffer(header_address, header_size, (unsigned char *) &header);

  return header;
}

int isFree(header_t header) {
  return header.flag != 1;
}

unsigned int getSize(header_t header) {
  return header.size;
}

unsigned int getNextBlock(unsigned int address) {
  unsigned int address_size = getSize(getHeader(address));
  unsigned int header_size = sizeof (header_t);

  return address + address_size + header_size;
}

unsigned int getPreviousBlock(unsigned int address) {
  unsigned int current = sizeof (header_t);
  unsigned int previous = 0;

  while (current != address) {
    previous = current;
    current = getNextBlock(current);
    if (current >= msize()) {
      return 0;
    }
  }

  return previous;
}

/**
 * Inicializacia pamate
 *
 * Zavola sa, v stave, ked sa zacina s prazdnou pamatou, ktora je inicializovana
 * na 0.
 */
void my_init(void) {
  unsigned int header_size = sizeof (header_t);
  header_t start;
  start.flag = 0;
  start.size = msize() - header_size;
  writeBuffer(0, header_size, (unsigned char *) &start);
}

/**
 * Poziadavka na alokaciu 'size' pamate. 
 *
 * Ak sa pamat podari alokovat, navratova hodnota je adresou prveho bajtu
 * alokovaneho priestoru v RAM. Pokial pamat uz nie je mozne alokovat, funkcia
 * vracia FAIL.
 */
int my_alloc(unsigned int size) {
  unsigned int header_t_size = sizeof (header_t);
  unsigned int current_address = sizeof (header_t);
  unsigned int best_fit_address = 0;
  unsigned int best_fit_size = msize();
  unsigned int current_block_size;

  if (size > msize() - current_address) {
    return FAIL;
  }

  /* Najdeme miesto ktore ma najvhodnejsiu velkost. */
  while (current_address < msize()) {
    header_t current_header = getHeader(current_address);
    current_block_size = getSize(current_header);
    if (isFree(current_header) && current_block_size >= size &&
            current_block_size < best_fit_size) {
      best_fit_address = current_address;
      best_fit_size = getSize(getHeader(current_address));
    }
    current_address = getNextBlock(current_address);
  }

  if (best_fit_address == 0) {
    return FAIL;
  }

  header_t best_fit_header = getHeader(best_fit_address);
  if (best_fit_header.size > size + header_t_size) {
    header_t next_header;
    next_header.flag = 0;
    next_header.size = best_fit_header.size - size - header_t_size;
    best_fit_header.size = size;

    writeBuffer(best_fit_address + size, header_t_size,
            (unsigned char *) &next_header);
  }

  best_fit_header.flag = 1;
  writeBuffer(best_fit_address - header_t_size, header_t_size,
          (unsigned char *) &best_fit_header);

  return best_fit_address;
}

/**
 * Poziadavka na uvolnenie alokovanej pamate na adrese 'addr'.
 *
 * Ak bola pamat zacinajuca na adrese 'addr' alokovana, my_free ju uvolni a
 * vrati OK. Ak je adresa 'addr' chybna (nezacina na nej ziadna alokovana
 * pamat), my_free vracia FAIL.
 */

int my_free(unsigned int address) {
  unsigned int header_t_size = sizeof (header_t);
  header_t header;

  if (address >= msize() || address < header_t_size ||
          isFree(header = getHeader(address))) {
    return FAIL;
  }
  unsigned int previous_block = getPreviousBlock(address);
  unsigned int next_block = getNextBlock(address);

  if (previous_block == 0 && address != header_t_size) {
    return FAIL;
  }
  if (isFree(getHeader(address))) {
    return FAIL;
  }
  header.flag = 0;

  header_t next_header;
  if (next_block < msize() && isFree((next_header = getHeader(next_block)))) {
    header.size += getSize(next_header) + header_t_size;
  }
  writeBuffer(address - header_t_size, header_t_size,
          (unsigned char *) &header);

  header_t prev_header;
  if (address != header_t_size && isFree((
          prev_header = getHeader(previous_block)))) {
    prev_header.size += getSize(header) + header_t_size;

    writeBuffer(previous_block - header_t_size, header_t_size,
            (unsigned char *) &prev_header);
  }

  return OK;
}
