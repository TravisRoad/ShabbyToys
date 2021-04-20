#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <time.h>
#include <unistd.h>

#define TIME_S_PER_DAY 86400
#define MAXBUFSIZE 1024

int opt_a = 0, opt_r = 0, opt_g = 0, pathc = 0;
int opt_h = 0x3f3f3f3f, opt_m = 0x3f3f3f3f;
int opt_l = -1;

int* p_opt[6] = {&opt_a, &opt_r, &opt_m, &opt_l, &opt_h, &opt_g};

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

void print(struct stat st, char* buf) {
  time_t now = time(NULL);
  if (st.st_size > opt_l && st.st_size < opt_h && (now - st.st_mtime) / TIME_S_PER_DAY < opt_m)
    printf("%16ld  %s\n", st.st_size, buf);
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
      char new_path[256], buf[256];
      strcpy(new_path, path), strcat(new_path, "/");
      strcpy(buf, pre_path), strcat(buf, dirp->d_name);  
      
      if (dirp->d_type == 4) {
        if ((opt_a && '.' == dirp->d_name[0]) || '.' != dirp->d_name[0])
          print(st,buf);
        if (opt_r && strcmp("..", dirp->d_name)!=0 && strcmp(".", dirp->d_name)!=0) {
          list(strcat(new_path, dirp->d_name), strcat(buf, "/"), dirp->d_name);
        }
      } else {
        list(strcat(new_path, dirp->d_name), pre_path, dirp->d_name);
      }
    }
  } else {
    char buf[256];
    strcpy(buf, pre_path);
    print(st,strcat(buf, filename));
  }
}

void listfile_in_cwd(char* filename) {
  char* cwd = getcwd(NULL, MAXBUFSIZE);
  strcat(cwd, "/");
  if (filename[0] == '/')
    list(filename, "", filename);
  else
    list(strcat(cwd, filename), "", filename);
  free(cwd);
}

int parse_args(int argc, char* argv[], char* pathv[], int* pathc) {
  int state = 0, flag = 0;
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
          pathv[(*pathc)++] = argv[i];
        break;
      default:
        *p_opt[state] = atoi(argv[i]);
        state = 0;
        break;
    }
  }
  return state;
}

int main(int argc, char* argv[]) {
  char* pathv[256];
  if (0 != parse_args(argc, argv, pathv, &pathc)) usage();
  if (pathc == 0)
    listfile_in_cwd("");
  else
    for (int i = 0; i < pathc; ++i) listfile_in_cwd(pathv[i]);
  return 0;
}
