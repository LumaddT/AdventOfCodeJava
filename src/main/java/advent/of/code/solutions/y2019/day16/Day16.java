package advent.of.code.solutions.y2019.day16;

import advent.of.code.solutions.Day;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Day16 implements Day {
    private static final int[] BASE_PATTERN = {0, 1, 0, -1};
    private static final int ITERATIONS = 100;
    private static final int REPETITIONS_PART_2 = 10_000;

    @Override
    public String part1(String inputString) {
        List<Integer> input = inputString.trim().chars().map(c -> c - '0').boxed().toList();

        List<Integer> phase = new ArrayList<>(input);
        List<List<Integer>> patterns = generatePatterns(phase.size());

        for (int phaseIndex = 0; phaseIndex < ITERATIONS; phaseIndex++) {
            List<Integer> newPhase = new ArrayList<>();

            for (int i = 0; i < phase.size(); i++) {
                newPhase.add(applyPattern(phase, patterns.get(i)));
            }

            phase = newPhase;
        }

        return String.join("", phase.stream().limit(8).map(d -> String.format("%d", d)).toList());
    }

    /**
     * This solution works on the assumption that the offset will always be at least equal to half of
     * inputString.length() * REPETITIONS_PART_2. If that is the case, the pattern for each relevant digit
     * is 000000...111111, i.e. each relevant term in the next phase is the sum mod 10 of its current value
     * and the values of all terms after it.
     */
    @Override
    public String part2(String inputString) {
        List<Integer> input = inputString.trim().chars().map(c -> c - '0').boxed().toList();

        int offset = 0;
        for (int i = 0; i < 7; i++) {
            offset *= 10;
            offset += input.get(i);
        }

        List<Integer> phase = new ArrayList<>();
        for (int i = 0; i < REPETITIONS_PART_2; i++) {
            phase.addAll(input);
        }

        if (offset < phase.size() / 2) {
            return "This solution does not work for the input provided.";
        }

        phase = phase.subList(offset, phase.size());

        for (int phaseIndex = 0; phaseIndex < ITERATIONS; phaseIndex++) {
            List<Integer> newPhase = new ArrayList<>();

            int phaseSum = phase.stream().mapToInt(a -> a).sum();
            newPhase.add(phaseSum % 10);

            for (int i = 1; i < phase.size(); i++) {
                phaseSum -= phase.get(i - 1);
                newPhase.add(phaseSum % 10);
            }

            phase = newPhase;
        }

        return String.join("", phase.stream().limit(8).map(d -> String.format("%d", d)).toList());
    }

    private List<List<Integer>> generatePatterns(int size) {
        List<List<Integer>> returnValue = new ArrayList<>();
        for (int patternIndex = 1; patternIndex <= size; patternIndex++) {
            List<Integer> pattern = new ArrayList<>();

            for (int i = 0; i <= size; i++) {
                pattern.add(BASE_PATTERN[(i / patternIndex) % BASE_PATTERN.length]);
            }

            pattern.removeFirst();
            returnValue.add(pattern);
        }

        return returnValue;
    }

    private int applyPattern(List<Integer> phase, List<Integer> pattern) {
        int total = 0;
        for (int i = 0; i < phase.size(); i++) {
            total += phase.get(i) * pattern.get(i);
        }

        return Math.abs(total) % 10;
    }
}
