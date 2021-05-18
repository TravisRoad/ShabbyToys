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
                        -e 's/<p class="calendar">\n<i class="icon icon-calendar">\n<\/i>\n<em class="t">\n\([^<>]*\)<\/em>\n/timestamp \1\n/g'> $filename
    cat $filename | sed -e 's/<a class="ctrl download" href="\([^<>"]*\)" [^<>]*>/download_url https:\/\/bing.ioliu.cn\1\n/g' > $filename
    set -x
    lz=$(cat $filename | egrep "(description)|(download_url)|(timestamp)" | sed -e 's/\(download_url\) \([^)]*\)/\1=\2\ndownload/g' -e 's/\(description\) \(.*\) $/\1="\2"/g' -e 's/\(timestamp\) /\1=/g')
    # lz=$(cat $filename | egrep "(description)|(JouxFog_ZH-CN9947036409)" | sed -e 's/\(download_url\) \([^)]*\)/\1=\2\ndownload/g' -e 's/\(description\) \(.*\)/\1=\2/g')
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