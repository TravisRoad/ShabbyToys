#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <time.h>
#include <unistd.h>

#define MAX_LENGTH 4096
#define TIME_S_PER_DAY 86400
#define MAXBUFSIZE 1024
#define NUM_OPT 6

int opt_a = 0, opt_r = 0, opt_g = 0;
int opt_h = 0x3f3f3f3f;
int opt_l = -1;
int opt_m = 0x3f3f3f3f;

char opt[NUM_OPT] = {'a', 'r', 'm', 'l', 'h', '-'};
int* p_opt[NUM_OPT] = {&opt_a, &opt_r, &opt_m, &opt_l, &opt_h, &opt_g};

// ret = stat(path, &st);
void usage() {
  printf(
      "LIST 1.0 by Travis, lxy2018montage@bupt.edu.cn, %s %s \
        \nUsage: list [OPTION]... [FILE]..., \
        \nList information about the FILEs (the current directory by default),\n \
        \n  -?        \tDisplay this help and exit, \
        \n  -a        \tDo not hide entries starting with .\
        \n  -r        \tList subdirectories recursively, \
        \n  -l <bytes>\tMinimum of file size, \
        \n  -h <bytes>\tMaximum of file size, \
        \n  -m <days> \tLimit file last modified time\n",
      __DATE__, __TIME__);
  exit(0);
}

void list(char* path, char* pre_path, char* filename) {
  DIR* dp;
  struct dirent* dirp;
  struct stat st;
  if (0 > stat(path, &st)) {
    printf("%s: No such file or directory\n", path);
    return;
  }
  if (S_ISDIR(st.st_mode)) {
    if ((dp = opendir(path)) == NULL) {
      printf("%s: No such file or directory\n", path);
      return;
    }
    while ((dirp = readdir(dp)) != NULL) {
      if (!opt_a && '.' == dirp->d_name[0]) continue;
      char new_path[256] = {0}, buf[256] = {0};
      strcat(new_path, path), strcat(new_path, "/");
      strcat(buf, pre_path), strcat(buf, dirp->d_name);
      if (dirp->d_type == 4) {
        if (opt_r && strcmp("..", dirp->d_name) && strcmp(".", dirp->d_name)) {
          list(strcat(new_path, dirp->d_name), strcat(buf, "/"), dirp->d_name);
        } else if (opt_a && '.' == dirp->d_name[0]) {
          printf("%16ld  %s\n", st.st_size, buf);
        }
      } else {
        list(strcat(new_path, dirp->d_name), pre_path, dirp->d_name);
      }
    }
  } else {
    char buf[256] = {0};
    time_t now = time(NULL);
    strcat(buf, pre_path);
    if (st.st_size > opt_l && st.st_size < opt_h &&
        (now - st.st_mtime) / TIME_S_PER_DAY < opt_m)
      printf("%16ld  %s\n", st.st_size, strcat(buf, filename));
  }
}

void listfile_in_cwd(char* filename, int* num) {
  char* cwd = getcwd(NULL, MAXBUFSIZE);
  strcat(cwd, "/");
  if (filename[0] == '/')
    list(filename, "", filename);
  else
    list(strcat(cwd, filename), "", filename);
  free(cwd);
  *num += 1;
}

void parse_args(int argc, char* argv[]) {
  int state = 0, num = 0, flag = 0;
  for (int i = 1; i < argc; ++i) {
    switch (state) {
      case 0:
        if (argv[i][0] == '-' && flag==0) {
          switch(argv[i][1]){
            case 'r': opt_r = 1; break;
            case 'a': opt_a = 1; break;
            case 'm': state = 2; break;
            case 'l': state = 3; break;
            case 'h': state = 4; break;
            case '-': flag = 1; break;
            default:  usage(); break;
          }
        } else
          listfile_in_cwd(argv[i], &num);
        break;
      default:
        *p_opt[state] = atoi(argv[i]);
        state = 0;
        break;
    }
  }
  if (0 != state) usage();
  if (!num) listfile_in_cwd("", &num);
}

int main(int argc, char* argv[]) {
  parse_args(argc, argv);
  return 0;
}
