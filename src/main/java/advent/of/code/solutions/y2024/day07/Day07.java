package advent.of.code.solutions.y2024.day07;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day07 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Equation> input = parseInputString(inputString);

        return String.format("%d", input.stream()
                .filter(e -> isSolvable(e, false))
                .mapToLong(Equation::Result)
                .sum());
    }

    @Override
    public String part2(String inputString) {
        Set<Equation> input = parseInputString(inputString);

        return String.format("%d", input.stream()
                .filter(e -> isSolvable(e, true))
                .mapToLong(Equation::Result)
                .sum());
    }

    private Set<Equation> parseInputString(String inputString) {
        String[] inputSplit = inputString.split("\n");
        Set<Equation> returnValue = new HashSet<>();

        for (String line : inputSplit) {
            String[] lineSplit = line.split(":? ");
            List<Long> parsedLine = Arrays.stream(lineSplit).map(Long::parseLong).toList();
            returnValue.add(new Equation(parsedLine.getFirst(), Collections.unmodifiableList(parsedLine.subList(1, parsedLine.size()))));
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private boolean isSolvable(Equation equation, boolean allowConcat) {
        if (equation.Numbers().size() == 1) {
            return equation.Numbers().getFirst() == equation.Result();
        }

        for (Operations operation : Operations.values()) {
            if (!allowConcat && operation == Operations.CONCATENATION) {
                continue;
            }

            long newFirst = switch (operation) {
                case ADDITION -> equation.Numbers().getFirst() + equation.Numbers().get(1);
                case MULTIPLICATION -> equation.Numbers().getFirst() * equation.Numbers().get(1);
                case CONCATENATION -> concatenate(equation.Numbers().getFirst(), equation.Numbers().get(1));
            };

            List<Long> newNumbers = new ArrayList<>(equation.Numbers().subList(2, equation.Numbers().size()));
            newNumbers.addFirst(newFirst);

            if (isSolvable(new Equation(equation.Result(), newNumbers), allowConcat)) {
                return true;
            }
        }

        return false;
    }

    private long concatenate(long left, long right) {
        int rightDigits = (int) Math.log10(right) + 1;
        return left * (long) Math.pow(10, rightDigits) + right;
    }
}
