package advent.of.code.solutions.y2019.day01;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day01 implements Day {
    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .mapToInt(Integer::parseInt)
                .map(m -> m / 3 - 2)
                .sum());
    }

    @Override
    public String part2(String inputString) {
        Set<Integer> input = Arrays.stream(inputString.split("\n"))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        int result = 0;

        for (int mass : input) {
            int partial = mass;

            while (partial >= 0) {
                partial = (partial / 3) - 2;
                if (partial > 0) {
                    result += partial;
                }
            }
        }

        return String.format("%d", result);
    }
}
