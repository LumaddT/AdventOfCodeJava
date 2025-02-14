# Advent of Code - Java

All my solutions for
the [2019](https://adventofcode.com/2019), [2020](https://adventofcode.com/2020), [2021](https://adventofcode.com/2021)
and [2024](https://adventofcode.com/2024) editions of [Advent of Code](https://adventofcode.com/), and some
for [2018](https://adventofcode.com/2018) and [2022](https://adventofcode.com/2021). I wrote the solutions for other
events in Python and C#, and those are tracked in their own projects. With all 3 projects combined all puzzles are
solved, except for the few I solved manually.

## Usage

### Input files

The program expects input files to be in the `input` directory, each in its appropriate year subdirectory. Each file
must be named its day number, always using 2 digits, and it must include the `.txt` extension. As an example, the
input file for day 6 of 2018 must be located at `./inputs/2018/06.txt`.

### Solution classes

Solution classes must implement the `Day` interface. The interface provides the methods `part1(String inputString)` and
`part2(String inputString)`, each of which should return, as a `String`, the answer for that part. Each method takes as
its only parameter the contents of the appropriate input file.

Solution classes must be contained in the `solutions` module. The solution module contains a module for each event,
named `yXXXX`, where `XXXX` is the year of the event. Each event module contains itself 25 modules, one per
day, each named `dayXX`, where `XX` is the day number written with 2 digits.

Solution classes must be named `DayXX`, where `XX` is the number of the day the solution is for, written with 2 digits.
That is the same as the day module name, capitalized.

### Execution

The solution to run can be chosen by changing the `YEAR` and `DAY` constants in `Main.java`. `PART` selects which part
should be solved: if set to `1` or `2` only the appropriate part will run. If set to `0` both parts will run, one after
the other. The output is rendered in the form

```
Year YYYY, day DD:
Part 1: [answer]
Part 2: [answer]
```

### Verify all

If the `VERIFY_ALL` variable is set to true, all available solutions will be verified against their answers. Answers
must be stored in the `answers` directory, which is structured the same as the `inputs` directory. Each answer file must
consist of exactly 2 lines: the first line must contain the answer for part 1, and the second line the answer to part 2.

The program runs all solution classes it can find and compares their output with the expected answer. It notifies when
answers do not match, and it reports how long each solution took to compute.