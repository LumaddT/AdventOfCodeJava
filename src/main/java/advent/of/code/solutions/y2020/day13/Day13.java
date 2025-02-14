package advent.of.code.solutions.y2020.day13;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day13 implements Day {
    @Override
    public String part1(String inputString) {
        int minTime = Integer.parseInt(inputString.split("\n")[0]);
        Set<Integer> buses = Arrays.stream(inputString.split("\n")[1].split(","))
                .filter(n -> !n.equals("x"))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        int minBus = -1;
        int minWait = Integer.MAX_VALUE;

        for (int bus : buses) {
            int waitTime = bus - (minTime % bus);
            if (waitTime < minWait) {
                minBus = bus;
                minWait = bus - (minTime % bus);
            }
        }

        return String.format("%d", minBus * minWait);
    }

    @Override
    public String part2(String inputString) {
        List<Tuple<Integer, Integer>> factorsAndRemainders = new ArrayList<>();

        String[] valueStrings = inputString.split("\n")[1].split(",");
        for (int i = 0; i < valueStrings.length; i++) {
            int value;
            try {
                value = Integer.parseInt(valueStrings[i]);
            } catch (NumberFormatException e) {
                continue;
            }

            int remainder = (value - i) % value;
            while (remainder < 0) {
                remainder += value;
            }

            factorsAndRemainders.add(new Tuple<>(value, remainder));
        }

        return String.format("%d", findResult(factorsAndRemainders));
    }

    private static long findResult(List<Tuple<Integer, Integer>> factorsAndRemainders) {
        long testNumber = factorsAndRemainders.getFirst().First() + factorsAndRemainders.getFirst().Second();
        int validTuples = 1;
        long increase = factorsAndRemainders.getFirst().First();

        while (validTuples < factorsAndRemainders.size()) {
            Tuple<Integer, Integer> tupleToTry = factorsAndRemainders.get(validTuples);
            testNumber += increase;

            if (testNumber % tupleToTry.First() == tupleToTry.Second()) {
                increase *= tupleToTry.First();
                validTuples++;
            }
        }

        return testNumber;
    }
}
