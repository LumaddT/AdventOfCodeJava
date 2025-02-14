package advent.of.code.solutions.y2021.day01;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day01 implements Day {
    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        int total = 0;

        for (int i = 1; i < input.size(); i++) {
            if (input.get(i) > input.get(i - 1)) {
                total++;
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        int total = 0;

        for (int i = 3; i < input.size(); i++) {
            if (input.get(i) + input.get(i - 1) + input.get(i - 2)
                    > input.get(i - 1) + input.get(i - 2) + input.get(i - 3)) {
                total++;
            }
        }

        return String.format("%d", total);
    }
}
