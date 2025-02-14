package advent.of.code.solutions.y2021.day07;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day07 implements Day {
    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(","))
                .map(Integer::parseInt)
                .toList();

        int minFuel = Integer.MAX_VALUE;

        for (int i = 0; i <= input.stream().mapToInt(n -> n).max().orElseThrow(); i++) {
            int lambdaI = i;
            int fuel = input.stream()
                    .mapToInt(n -> Math.abs(n - lambdaI))
                    .sum();

            if (fuel < minFuel) {
                minFuel = fuel;
            }
        }

        return String.format("%d", minFuel);
    }

    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(","))
                .map(Integer::parseInt)
                .toList();

        int minFuel = Integer.MAX_VALUE;

        for (int i = 0; i <= input.stream().mapToInt(n -> n).max().orElseThrow(); i++) {
            int lambdaI = i;
            int fuel = input.stream()
                    .mapToInt(n -> Math.abs(n - lambdaI) * (Math.abs(n - lambdaI) + 1) / 2)
                    .sum();

            if (fuel < minFuel) {
                minFuel = fuel;
            }
        }

        return String.format("%d", minFuel);
    }
}
