package advent.of.code.solutions.y2020.day01;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day01 implements Day {
    @Override
    public String part1(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        for (int i = 0; i < input.size() - 1; i++) {
            for (int j = i + 1; j < input.size(); j++) {
                if (input.get(i) + input.get(j) == 2020) {
                    return String.format("%d", input.get(i) * input.get(j));
                }
            }
        }

        throw new RuntimeException("Bad solution.");
    }

    @Override
    public String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("\n")).map(Integer::parseInt).toList();

        for (int i = 0; i < input.size() - 2; i++) {
            for (int j = i + 1; j < input.size() - 1; j++) {
                for (int k = j + 1; k < input.size(); k++) {
                    if (input.get(i) + input.get(j) + input.get(k) == 2020) {
                        return String.format("%d", input.get(i) * input.get(j) * input.get(k));
                    }
                }
            }
        }

        throw new RuntimeException("Bad solution.");
    }
}
