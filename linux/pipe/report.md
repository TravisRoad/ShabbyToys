<h1 align="center">第四次上机作业</h1>
<p align="center">姓名：路修远 班级：2018211303班 学号：2018211148号</p>

## 内容

使用fork(), exec(), dup2(), pipe() ，open()，wait()等系统调用编写C语言程序完成与下列shell命令等价的功能。
grep -v usr < /etc/passwd | wc -l > r.txt; cat r.txt 
> （提示：为简化编程，不需要用strtok断词，直接用execlp实现能达到shell命令相同功能的程序即可）
> 例如：execlp("grep", "grep", "-v", "usr", 0);

## 结果

```shell
root@iZ2zeic08951bcwm3rjmwiZ:~/ShabbyToys/linux/pipe# gcc pipe.c -o pipe
root@iZ2zeic08951bcwm3rjmwiZ:~/ShabbyToys/linux/pipe# ./pipe
3

root@iZ2zeic08951bcwm3rjmwiZ:~/ShabbyToys/linux/pipe# grep -v usr < /etc/passwd | wc -l > r.txt; cat r.txt 
3
root@iZ2zeic08951bcwm3rjmwiZ:~/ShabbyToys/linux/pipe# 
```
可以看到两次结果一致。

## 源代码

```C
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
```