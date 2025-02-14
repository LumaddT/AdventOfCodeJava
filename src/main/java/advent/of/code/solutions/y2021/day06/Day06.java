package advent.of.code.solutions.y2021.day06;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day06 implements Day {
    private static final int RESET_VALUE = 6;
    private static final int BIRTH_VALUE = 8;

    private static final int ITERATIONS_PART_1 = 80;
    private static final int ITERATIONS_PART_2 = 256;

    @Override
    public String part1(String inputString) {
        Map<Integer, Long> lanternfishes = parseInputString(inputString);

        for (int i = 0; i < ITERATIONS_PART_1; i++) {
            lanternfishes = passDay(lanternfishes);
        }

        return String.format("%d", lanternfishes.values().stream().mapToLong(v -> v).sum());
    }

    @Override
    public String part2(String inputString) {
        Map<Integer, Long> lanternfishes = parseInputString(inputString);

        for (int i = 0; i < ITERATIONS_PART_2; i++) {
            lanternfishes = passDay(lanternfishes);
        }

        return String.format("%d", lanternfishes.values().stream().mapToLong(v -> v).sum());
    }

    private Map<Integer, Long> parseInputString(String inputString) {
        List<Integer> input = Arrays.stream(inputString.trim().split(","))
                .map(Integer::parseInt)
                .toList();

        Map<Integer, Long> lanternfishes = new HashMap<>();

        for (int i = 0; i <= BIRTH_VALUE; i++) {
            int lambdaI = i;
            lanternfishes.put(i, input.stream().filter(l -> l == lambdaI).count());
        }

        return Collections.unmodifiableMap(lanternfishes);
    }

    private Map<Integer, Long> passDay(Map<Integer, Long> lanternfishes) {
        Map<Integer, Long> newLanternfishes = new HashMap<>();

        for (int i = 0; i < BIRTH_VALUE; i++) {
            newLanternfishes.put(i, lanternfishes.get(i + 1));
        }

        newLanternfishes.put(BIRTH_VALUE, lanternfishes.get(0));
        newLanternfishes.put(RESET_VALUE, newLanternfishes.get(RESET_VALUE) + lanternfishes.get(0));

        return Collections.unmodifiableMap(newLanternfishes);
    }
}
