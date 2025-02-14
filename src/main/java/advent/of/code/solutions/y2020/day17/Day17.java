package advent.of.code.solutions.y2020.day17;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.SetUtils;
import advent.of.code.utils.coordinates.Coordinate3;
import advent.of.code.utils.coordinates.Coordinate4;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day17 implements Day {
    private static final int ITERATIONS = 6;

    @Override
    public String part1(String inputString) {
        Set<Coordinate3> input = parseInputStringPart1(inputString);
        Set<Coordinate3> activeCells = new HashSet<>(input);

        for (int i = 0; i < ITERATIONS; i++) {
            performCyclePart1(activeCells);
        }

        return String.format("%d", activeCells.size());
    }

    @Override
    public String part2(String inputString) {
        Set<Coordinate4> input = parseInputStringPart2(inputString);
        Set<Coordinate4> activeCells = new HashSet<>(input);

        for (int i = 0; i < ITERATIONS; i++) {
            performCyclePart2(activeCells);
        }

        return String.format("%d", activeCells.size());
    }

    private Set<Coordinate3> parseInputStringPart1(String inputString) {
        Set<Coordinate3> returnValue = new HashSet<>();
        char[][] grid = ParsingUtils.toGrid(inputString);

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '#') {
                    returnValue.add(new Coordinate3(row, column, 0));
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private Set<Coordinate4> parseInputStringPart2(String inputString) {
        Set<Coordinate4> returnValue = new HashSet<>();
        char[][] grid = ParsingUtils.toGrid(inputString);

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '#') {
                    returnValue.add(new Coordinate4(row, column, 0, 0));
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private void performCyclePart1(Set<Coordinate3> activeCells) {
        Set<Coordinate3> nextCycleActive = new HashSet<>();
        Set<Coordinate3> coordinatesToCheck = activeCells.stream().flatMap(c -> c.adjacent().stream()).collect(Collectors.toSet());
        coordinatesToCheck.addAll(activeCells);

        for (Coordinate3 cellConsidered : coordinatesToCheck) {
            int activeAdjacent = SetUtils.intersection(cellConsidered.adjacent(), activeCells).size();

            if (activeCells.contains(cellConsidered) && activeAdjacent == 2 || activeAdjacent == 3) {
                nextCycleActive.add(cellConsidered);
            }
        }

        activeCells.clear();
        activeCells.addAll(nextCycleActive);
    }

    private void performCyclePart2(Set<Coordinate4> activeCells) {
        Set<Coordinate4> nextCycleActive = new HashSet<>();
        Set<Coordinate4> coordinatesToCheck = activeCells.stream().flatMap(c -> c.adjacent().stream()).collect(Collectors.toSet());
        coordinatesToCheck.addAll(activeCells);

        for (Coordinate4 cellConsidered : coordinatesToCheck) {
            int activeAdjacent = SetUtils.intersection(cellConsidered.adjacent(), activeCells).size();

            if (activeCells.contains(cellConsidered) && activeAdjacent == 2 || activeAdjacent == 3) {
                nextCycleActive.add(cellConsidered);
            }
        }

        activeCells.clear();
        activeCells.addAll(nextCycleActive);
    }
}
