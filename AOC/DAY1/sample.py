from itertools import combinations

with open("aoc1.txt", "r", encoding="utf-8") as file:
    data = [int(line) for line in file]


for combo in combinations(data, 2):
    if combo[0] + combo[1] == 2020:
        part1 = combo[0] * combo[1]
        break

# part1_bonus = max(i * j if i + j ==2020 else 0 for i in data for j in data)

# part2_bonus = max()
print(part1)

print(max(i * j if i + j == 2020 else 0 for i in data for j in data))
