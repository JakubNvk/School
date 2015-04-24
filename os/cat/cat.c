#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <stdbool.h>

pthread_cond_t cond_read = PTHREAD_COND_INITIALIZER;
pthread_cond_t cond_write = PTHREAD_COND_INITIALIZER;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
bool stopper;
char data[1048576];
int already_read = 0;

void *read_data(void *d) {
  int readed;
  while (1) {
    pthread_mutex_lock(&mutex);

    if (already_read > 0) {
      perror("Waiting for read");
      pthread_cond_wait(&cond_read, &mutex);
    }

    readed = read(0, data, 1048576);
    perror("Reading");
    already_read = readed;
    pthread_cond_signal(&cond_write);

    if (readed == 0) {
      stopper = true;
    }

    pthread_mutex_unlock(&mutex);

    if (readed == -1) {
      perror("Error while reading");
      pthread_exit(NULL);
    } else if (readed == 0) {
      pthread_exit(NULL);
    }
  }
}

void *write_data(void *d) {
  int written;
  while (1) {
    pthread_mutex_lock(&mutex);

    if (!stopper && already_read == 0) {
      pthread_cond_wait(&cond_write, &mutex);
    }

    written = write(1, data, already_read);
    perror("Writing");

    if (already_read == written) {
      perror("Waiting for write");
      pthread_cond_signal(&cond_read);
    }

    already_read -= written;
    pthread_mutex_unlock(&mutex);

    if (written == -1) {
      perror("Error while writing");
      pthread_exit(NULL);
    } else if (written == 0) {
      pthread_exit(NULL);
    }
  }
}

int main() {
  pthread_t input, output;
  int return_value = 0;
  return_value = pthread_create(&input, NULL, read_data, NULL);
  if (return_value != 0) {
    perror("Error creating input thread");
    return return_value;
  }
  return_value = pthread_create(&output, NULL, write_data, NULL);
  if (return_value != 0) {
    perror("Error creating output thread");
    return return_value;
  }

  return_value += pthread_join(input, NULL);
  return_value += pthread_join(output, NULL);
  return return_value;
}