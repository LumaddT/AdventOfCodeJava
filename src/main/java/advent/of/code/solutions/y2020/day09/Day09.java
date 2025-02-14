package advent.of.code.solutions.y2020.day09;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day09 implements Day {
    private static final int PREAMBLE = 25;

    @Override
    public String part1(String inputString) {
        List<Long> input = Arrays.stream(inputString.split("\n"))
                .map(Long::parseLong)
                .toList();

        return String.format("%d", findTarget(input));
    }

    @Override
    public String part2(String inputString) {
        List<Long> input = Arrays.stream(inputString.split("\n"))
                .map(Long::parseLong)
                .toList();

        long target = findTarget(input);

        return String.format("%d", findTargetIntervalMinPlusMax(input, target));
    }

    private long findTarget(List<Long> input) {
        for (int i = PREAMBLE; i < input.size(); i++) {
            long value = input.get(i);
            boolean isValid = false;

            for (int first = i - PREAMBLE; first < i - 1 && !isValid; first++) {
                for (int second = first + 1; second < i; second++) {
                    if (value == input.get(first) + input.get(second) && !input.get(first).equals(input.get(second))) {
                        isValid = true;
                        break;
                    }
                }
            }

            if (!isValid) {
                return value;
            }
        }

        throw new RuntimeException("Bad solution.");
    }

    private long findTargetIntervalMinPlusMax(List<Long> input, long target) {
        long currentSum = input.get(0) + input.get(1);
        int currentLow = 0;
        int currentHigh = 1;

        while (currentSum != target) {
            if (currentSum < target) {
                currentHigh++;
                currentSum += input.get(currentHigh);
            } else {
                currentSum -= input.get(currentLow);
                currentLow++;
            }
        }

        List<Long> targetInterval = input.subList(currentLow, currentHigh + 1);

        return targetInterval.stream().min(Long::compareTo).orElseThrow()
                + targetInterval.stream().max(Long::compareTo).orElseThrow();
    }
}
