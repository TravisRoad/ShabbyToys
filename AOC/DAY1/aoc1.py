"""
Advent of Code[About][Events][Shop][Settings][Log Out]TravisRoad 2*
          2020[Calendar][AoC++][Sponsors][Leaderboard][Stats]
Our sponsors help make Advent of Code possible:
GitHub - We're hiring engineers to make GitHub fast. Interested? Email fast@github.com with details of exceptional performance work you've done in the past.
--- Day 1: Report Repair ---
After saving Christmas five years in a row, you've decided to take a vacation at a nice resort on a tropical island. Surely, Christmas will go on without you.

The tropical island has its own currency and is entirely cash-only. The gold coins used there have a little picture of a starfish; the locals just call them stars. None of the currency exchanges seem to have heard of them, but somehow, you'll need to find fifty of these coins by the time you arrive so you can pay the deposit on your room.

To save your vacation, you need to get all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently, something isn't quite adding up.

Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.

For example, suppose your expense report contained the following:

1721
979
366
299
675
1456
In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces 1721 * 299 = 514579, so the correct answer is 514579.

Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you multiply them together?

Your puzzle answer was 539851.

--- Part Two ---
The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over from a past vacation. They offer you a second one if you can find three numbers in your expense report that meet the same criteria.

Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together produces the answer, 241861950.

In your expense report, what is the product of the three entries that sum to 2020?

Your puzzle answer was 212481360.

Both parts of this puzzle are complete! They provide two gold stars: **

At this point, you should return to your Advent calendar and try another puzzle.

If you still want to see it, you can get your puzzle input.

You can also [Share] this puzzle.

"""


def func():
    with open("aoc1.txt", 'r', encoding='utf-8') as f:
        lz = f.readlines()
    return list(map(lambda x: int(x), lz))


def search(sum: int, lz: list):
    resultSet = []
    length = len(lz)
    for i in range(0, length):
        if lz[i] > sum:
            continue
        for j in range(i + 1, length):
            if lz[j] > sum:
                continue
            if(lz[i] + lz[j] == sum):
                resultSet.append((lz[i], lz[j]))
    return resultSet


def aoc1():
    lz = func()
    ret = search(2020, lz)
    if len(ret) > 0:
        print(ret[0])


def aoc2():
    lz = func()
    for i in range(0, len(lz) - 1):
        p = 2020 - lz[i]
        ret = search(p, lz[i+1:])
        if len(ret) > 0:
            s = "{} + {} + {}".format(lz[i], ret[0][0], ret[0][1])
            print(s + " = {}".format(eval(s)))
            print(lz[i] * ret[0][0] * ret[0][1])


if __name__ == "__main__":
    aoc2()



