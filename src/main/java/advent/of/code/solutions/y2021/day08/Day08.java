package advent.of.code.solutions.y2021.day08;

import advent.of.code.solutions.Day;
import advent.of.code.utils.SetUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day08 implements Day {
    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .map(l -> l.split(" \\| ")[1].split(" "))
                .flatMap(Arrays::stream)
                .filter(s -> s.length() <= 4 || s.length() == 7)
                .count());
    }

    @Override
    public String part2(String inputString) {
        int total = 0;

        for (String line : inputString.split("\n")) {
            List<String> digitsToAdd = Arrays.stream(line.split(" \\| ")[1].split(" "))
                    .map(n -> n.chars()
                            .sorted()
                            .mapToObj(ch -> "" + (char) ch)
                            .collect(Collectors.joining("")))
                    .toList();

            List<Set<String>> tenDigits = Arrays.stream(line.split(" \\| ")[0].split(" "))
                    .map(n -> n.chars()
                            .mapToObj(ch -> "" + (char) ch)
                            .collect(Collectors.toSet()))
                    .toList();

            Map<String, Integer> decodedDigits = decodeDigits(tenDigits);

            for (int i = 0; i < digitsToAdd.size(); i++) {
                total += decodedDigits.get(digitsToAdd.get(i)) * (int) Math.pow(10, digitsToAdd.size() - 1 - i);
            }
        }

        return String.format("%d", total);
    }

    private Map<String, Integer> decodeDigits(List<Set<String>> tenDigits) {
        Map<String, Integer> returnValue = new HashMap<>();

        Set<String> one = tenDigits.stream().filter(s -> s.size() == 2).findAny().orElseThrow();
        returnValue.put(one.stream().sorted().collect(Collectors.joining("")), 1);

        Set<String> four = tenDigits.stream().filter(s -> s.size() == 4).findAny().orElseThrow();
        returnValue.put(four.stream().sorted().collect(Collectors.joining("")), 4);

        Set<String> seven = tenDigits.stream().filter(s -> s.size() == 3).findAny().orElseThrow();
        returnValue.put(seven.stream().sorted().collect(Collectors.joining("")), 7);

        Set<String> eight = tenDigits.stream().filter(s -> s.size() == 7).findAny().orElseThrow();
        returnValue.put(eight.stream().sorted().collect(Collectors.joining("")), 8);

        Set<Set<String>> twoThreeFive = tenDigits.stream().filter(s -> s.size() == 5).collect(Collectors.toSet());

        Set<String> two = null;
        Set<String> three = null;
        Set<String> five = null;

        for (Set<String> candidate : twoThreeFive) {
            if (SetUtils.intersection(four, candidate).size() == 2) {
                two = candidate;
            } else if (SetUtils.intersection(one, candidate).size() == 2) {
                three = candidate;
            } else if (SetUtils.intersection(four, candidate).size() == 3) {
                five = candidate;
            } else {
                throw new RuntimeException("Bad twoThreeFive search.");
            }
        }

        if (two == null || three == null || five == null) {
            throw new RuntimeException("Bad twoThreeFive search.");
        }

        returnValue.put(two.stream().sorted().collect(Collectors.joining("")), 2);
        returnValue.put(three.stream().sorted().collect(Collectors.joining("")), 3);
        returnValue.put(five.stream().sorted().collect(Collectors.joining("")), 5);


        Set<Set<String>> zeroSixNine = tenDigits.stream().filter(s -> s.size() == 6).collect(Collectors.toSet());

        Set<String> zero = null;
        Set<String> six = null;
        Set<String> nine = null;

        for (Set<String> candidate : zeroSixNine) {
            if (SetUtils.intersection(five, candidate).size() == 4) {
                zero = candidate;
            } else if (SetUtils.intersection(seven, candidate).size() == 2) {
                six = candidate;
            } else if (SetUtils.intersection(four, candidate).size() == 4) {
                nine = candidate;
            } else {
                throw new RuntimeException("Bad twoThreeFive search.");
            }
        }

        if (zero == null || six == null || nine == null) {
            throw new RuntimeException("Bad twoThreeFive search.");
        }

        returnValue.put(zero.stream().sorted().collect(Collectors.joining("")), 0);
        returnValue.put(six.stream().sorted().collect(Collectors.joining("")), 6);
        returnValue.put(nine.stream().sorted().collect(Collectors.joining("")), 9);

        return Collections.unmodifiableMap(returnValue);
    }
}
