package advent.of.code.solutions.y2020.day18;

import advent.of.code.solutions.Day;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Day18 implements Day {
    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .map(l -> l.replace(" ", ""))
                .mapToLong(this::evaluateExpressionPart1)
                .sum());
    }

    @Override
    public String part2(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .map(l -> l.replace(" ", ""))
                .mapToLong(this::evaluateExpressionPart2)
                .sum());
    }

    private long evaluateExpressionPart1(String expression) {
        expression = "0+" + expression;

        int i = 1;
        long lValue = expression.charAt(0) - '0';
        Operations operation;

        while (i < expression.length()) {
            operation = switch (expression.charAt(i)) {
                case '+' -> Operations.ADDITION;
                case '*' -> Operations.MULTIPLICATION;
                default -> throw new IllegalStateException("Unexpected value: " + expression.charAt(i));
            };

            i++;

            long rValue;
            if (expression.charAt(i) != '(') {
                rValue = expression.charAt(i) - '0';
                i++;
            } else {
                int layer = 1;
                int initialI = i;
                i++;
                while (layer > 0) {
                    switch (expression.charAt(i)) {
                        case '(' -> layer++;
                        case ')' -> layer--;
                    }
                    i++;
                }

                rValue = evaluateExpressionPart1(expression.substring(initialI + 1, i - 1));
            }

            switch (operation) {
                case ADDITION -> lValue += rValue;
                case MULTIPLICATION -> lValue *= rValue;
            }
        }

        return lValue;
    }

    private long evaluateExpressionPart2(String expression) {
        if (expression.length() == 1) {
            return Integer.parseInt(expression);
        }

        long total = 1;
        long partialTotal = 0;

        int i = 0;

        while (i < expression.length()) {
            char token = expression.charAt(i);

            if (token == '+') {
                i++;
            } else if (Character.isDigit(token)) {
                partialTotal += token - '0';
                i++;
            } else if (token == '*') {
                total *= partialTotal;
                partialTotal = 0;
                i++;
            } else if (token == '(') {
                int layer = 1;
                int initialI = i;
                i++;
                while (layer > 0) {
                    switch (expression.charAt(i)) {
                        case '(' -> layer++;
                        case ')' -> layer--;
                    }
                    i++;
                }

                partialTotal += evaluateExpressionPart2(expression.substring(initialI + 1, i - 1));
            } else {
                throw new RuntimeException("Bad part 2 expression evaluation.");
            }
        }

        total *= partialTotal;

        return total;
    }
}
