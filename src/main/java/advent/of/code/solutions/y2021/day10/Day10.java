package advent.of.code.solutions.y2021.day10;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day10 implements Day {
    private static final Set<Character> OPEN_PARENTHESES = Set.of('(', '[', '{', '<');
    private static final Set<Character> CLOSED_PARENTHESES = Set.of(')', ']', '}', '>');

    private static final Map<Character, Integer> POINTS_PART_1 = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );

    private static final Map<Character, Integer> POINTS_PART_2 = Map.of(
            ')', 1,
            ']', 2,
            '}', 3,
            '>', 4
    );

    private static final Map<Character, Character> OPEN_TO_CLOSE = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>'
    );

    private static final Map<Character, Character> CLOSE_TO_OPEN = Map.of(
            ')', '(',
            ']', '[',
            '}', '{',
            '>', '<'
    );

    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n"))
                .map(this::firstIllegal)
                .filter(Objects::nonNull)
                .mapToInt(POINTS_PART_1::get)
                .sum());
    }

    @Override
    public String part2(String inputString) {
        String[] input = Arrays.stream(inputString.split("\n"))
                .filter(l -> firstIllegal(l) == null)
                .toArray(String[]::new);

        List<Long> scores = Arrays.stream(input)
                .map(this::findClosingCharacters)
                .map(this::openToCloseSequence)
                .mapToLong(this::calculateScore)
                .sorted()
                .boxed()
                .toList();

        return String.format("%d", scores.get(scores.size() / 2));
    }

    private Character firstIllegal(String line) {
        Stack<Character> parentheses = new Stack<>();

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (OPEN_PARENTHESES.contains(ch)) {
                parentheses.add(ch);
            } else if (CLOSED_PARENTHESES.contains(ch)) {
                if (parentheses.pop() != CLOSE_TO_OPEN.get(ch)) {
                    return ch;
                }
            } else {
                throw new RuntimeException(String.format("Illegal character %c.", ch));
            }
        }

        return null;
    }

    private Stack<Character> findClosingCharacters(String line) {
        Stack<Character> parentheses = new Stack<>();

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (OPEN_PARENTHESES.contains(ch)) {
                parentheses.add(ch);
            } else if (CLOSED_PARENTHESES.contains(ch)) {
                if (parentheses.pop() != CLOSE_TO_OPEN.get(ch)) {
                    throw new RuntimeException("Did not exclude all corrupted lines.");
                }
            } else {
                throw new RuntimeException(String.format("Illegal character %c.", ch));
            }
        }

        return parentheses;
    }

    private Queue<Character> openToCloseSequence(Stack<Character> parentheses) {
        Queue<Character> returnValue = new LinkedList<>();

        while (!parentheses.isEmpty()) {
            returnValue.add(OPEN_TO_CLOSE.get(parentheses.pop()));
        }

        return returnValue;
    }

    private long calculateScore(Queue<Character> parentheses) {
        long score = 0;

        while (!parentheses.isEmpty()) {
            score *= 5;
            score += POINTS_PART_2.get(parentheses.poll());
        }

        return score;
    }
}
