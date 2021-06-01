#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char* argv[])
{
    int pfd[2], sv;
    pipe(pfd);
    if(fork() == 0){
        int fd0 = open("/etc/passwd", O_RDONLY);
        if(fd0 != -1) {
            dup2(fd0, 0);
            close(fd0);
        }
        dup2(pfd[1],1);
        close(pfd[1]);
        close(pfd[0]);
        execlp("grep", "grep", "-v", "usr", NULL);
        exit(1);
    }else if(fork() == 0){
        int fd1 = open("r.txt", O_CREAT | O_WRONLY, 0666);
        if(fd1 != -1){
            dup2(fd1,1);
            close(fd1);
        }
        dup2(pfd[0],0);
        close(pfd[1]);
        close(pfd[0]);
        execlp("wc", "wc", "-l", NULL);
        exit(1);
    }
    close(pfd[0]);
    close(pfd[1]);
    wait(&sv);
    wait(&sv);
    execlp("cat", "cat", "r.txt", NULL);
}