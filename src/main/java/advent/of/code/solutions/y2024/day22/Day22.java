package advent.of.code.solutions.y2024.day22;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day22 implements Day {
    private static final int ITERATIONS = 2_000;

    //    private static final int STEP_1_CONSTANT = 64;
    private static final int STEP_1_BIT_SHIFT = 6;
    //    private static final int STEP_2_CONSTANT = 32;
    private static final int STEP_2_BIT_SHIFT = 5;
    //    private static final int STEP_3_CONSTANT = 2048;
    private static final int STEP_3_BIT_SHIFT = 11;

    private static final int PRUNE_CONSTANT = 16_777_216;

    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        long total = 0;

        for (int value : input) {
            for (int i = 0; i < ITERATIONS; i++) {
                value = step3(step2(step1(value)));
            }

            total += value;
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        List<List<Integer>> sequences = new ArrayList<>();

        for (int value : input) {
            List<Integer> newList = new ArrayList<>();
            newList.add(value % 10);
            for (int i = 0; i < ITERATIONS; i++) {
                value = step3(step2(step1(value)));
                newList.add(value % 10);
            }

            sequences.add(newList);
        }

        Map<Changes, Integer> changesToBananas = new HashMap<>();

        for (List<Integer> sequence : sequences) {
            Set<Changes> changesSeen = new HashSet<>();
            Changes changes = new Changes(0,
                    sequence.get(1) - sequence.get(0),
                    sequence.get(2) - sequence.get(1),
                    sequence.get(3) - sequence.get(2));

            for (int i = 4; i < sequence.size(); i++) {
                int change = sequence.get(i - 1) - sequence.get(i);
                changes = changes.insertLast(change);

                if (changesSeen.contains(changes)) {
                    continue;
                }

                changesSeen.add(changes);

                int bananas = sequence.get(i);

                if (changesToBananas.containsKey(changes)) {
                    changesToBananas.put(changes, changesToBananas.get(changes) + bananas);
                } else {
                    changesToBananas.put(changes, bananas);
                }
            }
        }

        return String.format("%d", changesToBananas.values().stream()
                .max(Integer::compareTo)
                .orElseThrow());
    }

    public int step1(long secret) {
        long result = secret << STEP_1_BIT_SHIFT;
        secret ^= result;
        return (int) (secret % PRUNE_CONSTANT);
    }

    public int step2(long secret) {
        long result = secret >> STEP_2_BIT_SHIFT;
        secret ^= result;
        return (int) (secret % PRUNE_CONSTANT);
    }

    public int step3(long secret) {
        long result = secret << STEP_3_BIT_SHIFT;
        secret ^= result;
        return (int) (secret % PRUNE_CONSTANT);
    }
}
