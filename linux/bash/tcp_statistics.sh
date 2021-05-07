#!/usr/bin/bash
clear
interval=1
while (true)
	do 
	str=`netstat --statistics | grep "[0-9][0-9]* segments received"`
	tcp_recv=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	str=`netstat --statistics | grep "[0-9][0-9]* segments sent out"`
	tcp_out=$(expr "$str" : '^ *\([0-9][0-9]*\).*$')
	echo tcp_recv = $tcp_recv tcp_out = $tcp_out
	sleep $interval
done
