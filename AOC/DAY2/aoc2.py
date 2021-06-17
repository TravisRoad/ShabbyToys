with open('data.txt', 'r', encoding='utf-8') as file:
    data = file.readlines()
passWdList = []
for line in data:
    lz = line.split()
    theRange = [int(i) for i in lz[0].split('-')]
    key = lz[1][:-1]
    passwd = lz[2]
    # print(theRange, key, passwd)
    passWdList.append(
        {'min': theRange[0], 'max': theRange[1], 'key': key, 'passwd': passwd})
# print(passWdList)
part1_cnt = 0
for line in passWdList:
    if line['passwd'].count(line['key']) in range(line['min'], line['max']+1):
        print(line)
        part1_cnt += 1

print(len(passWdList))
print(part1_cnt)

part2_cnt = 0
for line in passWdList:
    i, j = line['min'] - 1, line['max'] - 1
    passwd = line['passwd']
    key = line['key']
    if (passwd[i] != key and passwd[j] == key) or (passwd[i] == key and passwd[j] != key):
        part2_cnt += 1
        print(line)

print(part2_cnt)
