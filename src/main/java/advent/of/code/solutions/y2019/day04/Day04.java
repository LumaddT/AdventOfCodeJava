package advent.of.code.solutions.y2019.day04;

import advent.of.code.solutions.Day;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class Day04 implements Day {
    @Override
    public String part1(String inputString) {
        int min = Integer.parseInt(inputString.split("-")[0].trim());
        int max = Integer.parseInt(inputString.split("-")[1].trim());

        return String.format("%d", IntStream.range(min, max + 1)
                .filter(this::meetsCriteriaPart1)
                .count());
    }

    @Override
    public String part2(String inputString) {
        int min = Integer.parseInt(inputString.split("-")[0].trim());
        int max = Integer.parseInt(inputString.split("-")[1].trim());

        return String.format("%d", IntStream.range(min, max + 1)
                .filter(this::meetsCriteriaPart2)
                .count());
    }

    private boolean meetsCriteriaPart1(int password) {
        int[] digits = String.format("%d", password).chars().toArray();

        if (digitsDescending(digits)) {
            return false;
        }

        for (int i = 1; i < digits.length; i++) {
            if (digits[i] == digits[i - 1]) {
                return true;
            }
        }

        return false;
    }

    private boolean meetsCriteriaPart2(int password) {
        int[] digits = String.format("%d", password).chars().toArray();

        if (digitsDescending(digits)) {
            return false;
        }

        int currentGroup = 1;

        for (int i = 1; i < digits.length; i++) {
            if (digits[i] == digits[i - 1]) {
                currentGroup++;
            } else if (currentGroup == 2) {
                return true;
            } else {
                currentGroup = 1;
            }
        }

        return currentGroup == 2;
    }

    private boolean digitsDescending(int[] digits) {
        for (int i = 1; i < digits.length; i++) {
            if (digits[i] < digits[i - 1]) {
                return true;
            }
        }

        return false;
    }
}
