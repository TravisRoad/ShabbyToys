<h1 align="center"> shell脚本 </h1>

姓名：路修远 班级：2018211303班 学号：2018211148号

-----

[TOC]

## 第一题：生成TCP活动状况报告

### 运行结果

![](http://image.lxythan2lxy.cn/image-20210518130112950.png)

### 源代码

```bash
#!/usr/bin/bash
clear
interval=60
str=`netstat --statistics | grep "[0-9][0-9]* segments received"`
recv=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
out=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')

# comp pre_sum sum return
comp()
{
	if [ $2 -gt $(expr $1 + 10) ]
	then
		eval "$3=\"+\""	
	elif [ $2 -gt $(expr $1 - 10) ]
	then
		eval "$3=\" \""
	else
		eval "$3=\"-\""
	fi
}

#Usage: output tcp_recv tcp_out recv out RECV OUT
output()
{
	outnum=`expr $2 - $4`
	recvnum=`expr $1 - $3`
	sum=`expr $outnum + $recvnum`
	if [ $7 -gt 0 ]
	then
		comp $pre_sum $sum "flag"
    fi
	pre_sum=$sum
	eval "$5=$1";eval "$6=$2"
	echo -e "\r" `date "+%Y-%m-%d %H:%M"` "\c"
	printf "%4d %4d %4d %4c\n" $outnum $recvnum $sum $flag
}

index=0
while (true)
	do
	sleep $interval
	str=`netstat --statistics | grep "[0-9][0-9]* segments received"`
	tcp_recv=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	tcp_out=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	output $tcp_recv $tcp_out $recv $out "recv" "out" $index
	index=$(expr $index + 1)
done
```
##  第二题：下载bing图库中图片

### 效果

![image-20210518185536598](http://image.lxythan2lxy.cn/image-20210518185536598.png)

### 源代码

```shell
#usage: download
download()
{
    if [ ! -f $description.jpg ]; then
        wget -c -O "$timestamp $description.jpg" $download_url
    fi
}

#usage: spider
spider()
{
    filename=page$page.html
    # if [ ! -f $filename ]; then
    rm -f $filename
    wget -c -O $filename "$source_url/?p=$page"
    # fi
    # <p class="calendar"><i class="icon icon-calendar"></i><em class="t">2021-03-31</em></p>
    cat $filename | sed -e 's/\(<[^<>]*>\)/\1\n/g' -e 's/\(<div class="description">\)\n/\1/g' \
                        -e 's/\(<div class="description"><h3>\)\n/\1/g' \
                        -e 's/<div class="description"><h3>\([^()<>]*\)([^()]*)<\/h3>/description \1/g' \
                        -e 's/<p class="calendar">\n<i class="icon icon-calendar">\n<\/i>\n<em class="t">\n\([^<>]*\)<\/em>\n/timestamp \1\n/g' \
                        -e 's/<a class="ctrl download" href="\([^<>"]*\)" [^<>]*>/download_url https:\/\/bing.ioliu.cn\1\n/g' > /tmp/bingtmp.out
    lz=$(cat /tmp/bingtmp.out | egrep "(description)|(download_url)|(timestamp)" | sed -e 's/\(download_url\) \([^)]*\)/\1=\2\ndownload/g' -e 's/\(description\) \(.*\) $/\1="\2"/g' -e 's/\(timestamp\) /\1=/g')
    echo $lz > out
    IFS=$'\n'
    for i in $lz
    do
        eval $i
    done
    rm -f $filename
}
source_url="https://bing.ioliu.cn"
if [ $# -gt 0 ]; then
    start=$1;end=$2
else
    start=1;end=140
fi
OLDIFS=$IFS
for page in `seq $start $end`; do
    spider
    IFS=$OLDIFS
done
```

