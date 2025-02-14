package advent.of.code.solutions.y2024.day11;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day11 implements Day {
    private static final int ITERATION_PART_1 = 25;
    private static final int ITERATION_PART_2 = 75;

    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(" ")).map(Integer::parseInt).toList();

        long total = 0;

        for (Integer value : input) {
            total += blink(value, ITERATION_PART_1);
        }

        return String.format("%d", total);
    }


    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(" ")).map(Integer::parseInt).toList();

        long total = 0;

        for (Integer value : input) {
            total += blink(value, ITERATION_PART_2);
        }

        return String.format("%d", total);
    }

    private long blink(long value, int iterations) {
        return blink(value, iterations, new HashMap<>());
    }

    private long blink(long value, int iterations, Map<Tuple<Long, Integer>, Long> cache) {
        if (iterations <= 0) {
            return 1;
        }

        Tuple<Long, Integer> valueIterations = new Tuple<>(value, iterations);
        if (cache.containsKey(valueIterations)) {
            return cache.get(valueIterations);
        }

        long returnValue;

        if (value == 0) {
            returnValue = blink(1, iterations - 1, cache);
        } else if (countDigits(value) % 2 == 0) {
            int digitsPerStone = countDigits(value) / 2;
            long thisNumber = (long) (value / Math.pow(10, digitsPerStone));
            long otherNumber = (long) (value - (thisNumber * Math.pow(10, digitsPerStone)));

            returnValue = blink(thisNumber, iterations - 1, cache) + blink(otherNumber, iterations - 1, cache);
        } else {
            returnValue = blink(value * 2024, iterations - 1, cache);
        }

        cache.put(valueIterations, returnValue);
        return returnValue;
    }

    private int countDigits(long value) {
        return (int) Math.floor(Math.log10(value)) + 1;
    }
}
