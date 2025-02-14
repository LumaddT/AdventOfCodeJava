package advent.of.code.solutions.y2021.day14;

import advent.of.code.solutions.Day;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day14 implements Day {
    private static final int ITERATIONS_PART_1 = 10;
    private static final int ITERATIONS_PART_2 = 40;

    @Override
    public String part1(String inputString) {
        Map<String, Long> template = parseTemplate(inputString.split("\n\n")[0]);
        Map<String, Set<String>> rules = parseRules(inputString.split("\n\n")[1]);
        Set<Character> elements = inputString.chars()
                .mapToObj(ch -> (char) ch)
                .filter(Character::isLetter)
                .collect(Collectors.toSet());

        char firstLetter = inputString.split("\n\n")[0].charAt(0);

        for (int i = 0; i < ITERATIONS_PART_1; i++) {
            template = applyRules(rules, template);
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (char element : elements) {
            long candidate = template.keySet().stream()
                    .filter(pair -> pair.charAt(1) == element)
                    .mapToLong(template::get)
                    .sum();

            if (element == firstLetter) {
                candidate++;
            }

            min = Math.min(min, candidate);
            max = Math.max(max, candidate);
        }

        return String.format("%d", max - min);
    }

    @Override
    public String part2(String inputString) {
        Map<String, Long> template = parseTemplate(inputString.split("\n\n")[0]);
        Map<String, Set<String>> rules = parseRules(inputString.split("\n\n")[1]);
        Set<Character> elements = inputString.chars()
                .mapToObj(ch -> (char) ch)
                .filter(Character::isLetter)
                .collect(Collectors.toSet());

        char firstLetter = inputString.split("\n\n")[0].charAt(0);

        for (int i = 0; i < ITERATIONS_PART_2; i++) {
            template = applyRules(rules, template);
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (char element : elements) {
            long candidate = template.keySet().stream()
                    .filter(pair -> pair.charAt(1) == element)
                    .mapToLong(template::get)
                    .sum();

            if (element == firstLetter) {
                candidate++;
            }

            min = Math.min(min, candidate);
            max = Math.max(max, candidate);
        }

        return String.format("%d", max - min);
    }

    private Map<String, Long> parseTemplate(String inputString) {
        Map<String, Long> returnValue = new HashMap<>();

        for (int i = 1; i < inputString.length(); i++) {
            String pair = "" + inputString.charAt(i - 1) + inputString.charAt(i);
            returnValue.putIfAbsent(pair, 0L);
            returnValue.put(pair, returnValue.get(pair) + 1);
        }

        return Collections.unmodifiableMap(returnValue);
    }

    private Map<String, Set<String>> parseRules(String inputString) {
        Map<String, Set<String>> rules = new HashMap<>();

        for (String line : inputString.split("\n")) {
            String from = line.split(" -> ")[0];
            String insertionLetter = line.split(" -> ")[1];

            Set<String> result = Set.of(
                    from.charAt(0) + insertionLetter,
                    insertionLetter + from.charAt(1)
            );

            rules.put(from, result);
        }

        return Collections.unmodifiableMap(rules);
    }

    private Map<String, Long> applyRules(Map<String, Set<String>> rules, Map<String, Long> template) {
        Map<String, Long> returnValue = new HashMap<>();

        for (String key : template.keySet()) {
            for (String result : rules.get(key)) {
                returnValue.putIfAbsent(result, 0L);
                returnValue.put(result, returnValue.get(result) + template.get(key));
            }
        }

        return Collections.unmodifiableMap(returnValue);
    }
}
