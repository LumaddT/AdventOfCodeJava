package advent.of.code.solutions.y2022.day19;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Day19 implements Day {
    private static final int MAX_TIME_PART_1 = 24;

    private static final int MAX_TIME_PART_2 = 32;
    private static final int BLUEPRINTS_LIMIT_PART_2 = 3;

    @Override
    public String part1(String inputString) {
        List<Blueprint> input = Arrays.stream(inputString.split("\n"))
                .map(l -> l.split(": ")[1])
                .map(Blueprint::new)
                .toList();

        int total = 0;

        for (int i = 0; i < input.size(); i++) {
            total += input.get(i).calculateMaxOre(MAX_TIME_PART_1) * (i + 1);
        }

        return String.format("%d", total);
    }

    /**
     * About 25 seconds on my machine. Not too awful. The fact that part 2 wants you to look at
     * only the first 3 blueprints makes me think it may even be intended to take more than most solutions.
     */
    @Override
    public String part2(String inputString) {
        List<Blueprint> input = Arrays.stream(inputString.split("\n"))
                .limit(BLUEPRINTS_LIMIT_PART_2)
                .map(l -> l.split(": ")[1])
                .map(Blueprint::new)
                .toList();

        int total = 1;

        for (Blueprint blueprint : input) {
            total *= blueprint.calculateMaxOre(MAX_TIME_PART_2);
        }

        return String.format("%d", total);
    }
}
