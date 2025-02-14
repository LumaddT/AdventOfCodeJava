package advent.of.code.solutions.y2024.day13;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day13 implements Day {
    @Override
    public String part1(String inputString) {
        Set<ClawMachine> input = Arrays.stream(inputString.split("\n\n")).map(s -> new ClawMachine(s, true)).collect(Collectors.toSet());
        long total = 0;
        for (ClawMachine clawMachine : input) {
            total += clawMachine.getTotalCost();
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Set<ClawMachine> input = Arrays.stream(inputString.split("\n\n")).map(s -> new ClawMachine(s, false)).collect(Collectors.toSet());
        long total = 0;

        for (ClawMachine clawMachine : input) {
            total += clawMachine.getTotalCost();
        }

        return String.format("%d", total);
    }
}
