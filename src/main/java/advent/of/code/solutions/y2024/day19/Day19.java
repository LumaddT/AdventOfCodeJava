package advent.of.code.solutions.y2024.day19;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Day19 implements Day {
    @Override
    public String part1(String inputString) {
        String[] towels = inputString.split("\n\n")[0].split(", ");
        String[] patterns = inputString.split("\n\n")[1].split("\n");

        return String.format("%d", Arrays.stream(patterns)
                .filter(p -> isPatternValid(p, towels))
                .count());
    }

    @Override
    public String part2(String inputString) {
        String[] towels = inputString.split("\n\n")[0].split(", ");
        String[] patterns = inputString.split("\n\n")[1].split("\n");

        Map<String, Long> cache = new HashMap<>();

        return String.format("%d", Arrays.stream(patterns)
                .mapToLong(p -> countWaysToMakePattern(p, towels, cache))
                .sum());
    }

    private boolean isPatternValid(String pattern, String[] towels) {
        if (pattern.isEmpty()) {
            return true;
        }

        for (String towel : towels) {
            if (pattern.startsWith(towel)) {
                if (isPatternValid(pattern.substring(towel.length()), towels)) {
                    return true;
                }
            }
        }

        return false;
    }

    private long countWaysToMakePattern(String pattern, String[] towels, Map<String, Long> cache) {
        if (pattern.isEmpty()) {
            return 1;
        }

        if (cache.containsKey(pattern)) {
            return cache.get(pattern);
        }

        long total = 0;

        for (String towel : towels) {
            if (pattern.startsWith(towel)) {
                total += countWaysToMakePattern(pattern.substring(towel.length()), towels, cache);
            }
        }

        cache.put(pattern, total);
        return total;
    }
}
