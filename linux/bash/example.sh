if [ $# = 0 ]
then
echo "Usage: $0 : <number>"
else
count=$1
while [ $count -gt 0 ]
do
count=`expr $count - 1`
echo -e "\015 $count \c"
sleep 1
done
fi
