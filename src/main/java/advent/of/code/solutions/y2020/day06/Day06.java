package advent.of.code.solutions.y2020.day06;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day06 implements Day {
    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n\n"))
                .mapToInt(
                        g -> Arrays.stream(g.split("\n"))
                                .flatMap(p -> p.chars().boxed())
                                .collect(Collectors.toSet())
                                .size()
                )
                .sum());
    }

    @Override
    public String part2(String inputString) {
        List<List<List<Character>>> input = Arrays.stream(inputString.split("\n\n"))
                .map(
                        g -> Arrays.stream(g.split("\n"))
                                .map(
                                        p -> p.chars()
                                                .mapToObj(c -> (char) c)
                                                .toList()
                                )
                                .toList()
                )
                .toList();

        int total = 0;

        for (List<List<Character>> group : input) {
            for (char question : group.getFirst()) {
                boolean isYes = true;

                for (List<Character> person : group) {
                    if (!person.contains(question)) {
                        isYes = false;
                        break;
                    }
                }

                if (isYes) {
                    total++;
                }
            }
        }

        return String.format("%d", total);
    }
}
