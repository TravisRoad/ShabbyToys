#!/usr/bin/bash
clear
interval=1
str=`netstat --statistics | grep "[0-9][0-9]* segments received"`
recv=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
str=`netstat --statistics | grep "[0-9][0-9]* segments sent out"`
out=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')

#Usage: output tcp_recv tcp_out recv out RECV OUT flag
output()
{
	outnum=`expr $2 - $4`
	recvnum=`expr $1 - $3`
	sum=`expr $outnum + $recvnum`
	eval "$5=$1";eval "$6=$2"
	[ $7 -eq 0 ] && echo `date "+%Y-%m-%d %H:%M"` $outnum $recvnum $sum  
}


while (true)
	do 
	str=`netstat --statistics | grep "[0-9][0-9]* segments received"`
	tcp_recv=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	str=`netstat --statistics | grep "[0-9][0-9]* segments sent out"`
	tcp_out=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	output $tcp_recv $tcp_out $recv $out "recv" "out" 0 0 
	sleep $interval
done
