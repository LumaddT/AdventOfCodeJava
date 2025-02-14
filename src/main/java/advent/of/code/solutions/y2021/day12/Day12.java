package advent.of.code.solutions.y2021.day12;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day12 implements Day {
    private static final String START_NAME = "start";
    private static final String END_NAME = "end";

    @Override
    public String part1(String inputString) {
        Cave start = parseInputString(inputString);

        return String.format("%d", calculateAllPathsPart1(start, new HashSet<>()));
    }

    @Override
    public String part2(String inputString) {
        Cave start = parseInputString(inputString);

        return String.format("%d", calculateAllPathsPart2(start, new HashSet<>(), false));
    }

    private Cave parseInputString(String inputString) {
        Map<String, Cave> caves = Arrays.stream(inputString.split("\n"))
                .flatMap(l -> Arrays.stream(l.split("-")))
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toMap(l -> l, Cave::new));

        Arrays.stream(inputString.split("\n"))
                .map(l -> l.split("-"))
                .toList()
                .forEach(l -> caves.get(l[0]).addNeighbor(caves.get(l[1])));

        return caves.get(START_NAME);
    }

    private int calculateAllPathsPart1(Cave currentCave, Set<Cave> smallVisited) {
        if (currentCave.getName().equals(END_NAME)) {
            return 1;
        }

        if (currentCave.isSmall()) {
            smallVisited.add(currentCave);
        }

        int total = 0;

        for (Cave neighbor : currentCave.getNeighbors()) {
            if (smallVisited.contains(neighbor)) {
                continue;
            }

            total += calculateAllPathsPart1(neighbor, new HashSet<>(smallVisited));
        }

        return total;
    }

    private int calculateAllPathsPart2(Cave currentCave, Set<Cave> visitedSmall, boolean visitedTwice) {
        if (currentCave.getName().equals(END_NAME)) {
            return 1;
        }

        if (currentCave.isSmall()) {
            visitedSmall.add(currentCave);
        }

        int total = 0;

        for (Cave neighbor : currentCave.getNeighbors()) {
            if (!neighbor.getName().equals(START_NAME) && visitedSmall.contains(neighbor) && !visitedTwice) {
                total += calculateAllPathsPart2(neighbor, new HashSet<>(visitedSmall), true);
            } else if (!visitedSmall.contains(neighbor)) {
                total += calculateAllPathsPart2(neighbor, new HashSet<>(visitedSmall), visitedTwice);
            }
        }

        return total;
    }
}
