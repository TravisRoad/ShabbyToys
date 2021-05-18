set -x
lz="description=汝拉山中的Chateau de Joux堡，法国 \ndownload_url=https://bing.ioliu.cn/photo/JouxFog_ZH-CN9947036409?force=download\ndownload"
IFS=$'\n'
for i in $lz; do
echo $i
done