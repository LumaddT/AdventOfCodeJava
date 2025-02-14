package advent.of.code.solutions.y2020.day15;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day15 implements Day {
    private static final int TURNS_PART_1 = 2020;
    private static final int TURNS_PART_2 = 30_000_000;

    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(",")).map(Integer::parseInt).toList();
        Map<Integer, Integer> memory = new HashMap<>();

        for (int i = 0; i < input.size() - 1; i++) {
            memory.put(input.get(i), i);
        }

        int lastNumber = input.getLast();

        for (int turn = input.size(); turn < TURNS_PART_1; turn++) {
            int numberSpoken;

            if (!memory.containsKey(lastNumber)) {
                numberSpoken = 0;
            } else {
                numberSpoken = turn - memory.get(lastNumber) - 1;
            }

            memory.put(lastNumber, turn - 1);
            lastNumber = numberSpoken;
        }

        return String.format("%d", lastNumber);
    }

    /**
     * I'm certain there's a better solution, but this takes less than
     * 20 seconds to execute, and it didn't require any extra thinking.
     */
    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(",")).map(Integer::parseInt).toList();
        Map<Integer, Integer> memory = new HashMap<>();

        for (int i = 0; i < input.size() - 1; i++) {
            memory.put(input.get(i), i);
        }

        int lastNumber = input.getLast();

        for (int turn = input.size(); turn < TURNS_PART_2; turn++) {
            int numberSpoken;

            if (!memory.containsKey(lastNumber)) {
                numberSpoken = 0;
            } else {
                numberSpoken = turn - memory.get(lastNumber) - 1;
            }

            memory.put(lastNumber, turn - 1);
            lastNumber = numberSpoken;
        }

        return String.format("%d", lastNumber);
    }
}
