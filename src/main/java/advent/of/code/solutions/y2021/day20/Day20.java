package advent.of.code.solutions.y2021.day20;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.SetUtils;
import advent.of.code.utils.ntuples.Tuple;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Day20 implements Day {
    private static final int ITERATIONS_PART_1 = 2;
    private static final int ITERATIONS_PART_2 = 50;

    @Override
    public String part1(String inputString) {
        List<Boolean> algorithm = inputString.split("\n\n")[0].chars()
                .mapToObj(ch -> (char) ch)
                .map(ch -> ch == '#')
                .toList();

        if (!algorithm.getFirst() || algorithm.getLast()) {
            throw new RuntimeException("The solution assumes the algorithm starts with active and ends with inactive.");
        }

        Tuple<Set<Coordinate>, Set<Coordinate>> image = parseImage(inputString.split("\n\n")[1]);
        Set<Coordinate> active = new HashSet<>(image.First());
        Set<Coordinate> inactive = new HashSet<>(image.Second());
        boolean defaultState = false;

        for (int i = 0; i < ITERATIONS_PART_1; i++) {
            applyAlgorithm(active, inactive, algorithm, defaultState);
            defaultState = !defaultState;
        }

        return String.format("%d", active.size());
    }

    @Override
    public String part2(String inputString) {
        List<Boolean> algorithm = inputString.split("\n\n")[0].chars()
                .mapToObj(ch -> (char) ch)
                .map(ch -> ch == '#')
                .toList();

        if (!algorithm.getFirst() || algorithm.getLast()) {
            throw new RuntimeException("The solution assumes the algorithm starts with active and ends with inactive.");
        }

        Tuple<Set<Coordinate>, Set<Coordinate>> image = parseImage(inputString.split("\n\n")[1]);
        Set<Coordinate> active = new HashSet<>(image.First());
        Set<Coordinate> inactive = new HashSet<>(image.Second());
        boolean defaultState = false;

        for (int i = 0; i < ITERATIONS_PART_2; i++) {
            applyAlgorithm(active, inactive, algorithm, defaultState);
            defaultState = !defaultState;
        }

        return String.format("%d", active.size());
    }

    private Tuple<Set<Coordinate>, Set<Coordinate>> parseImage(String inputString) {
        Set<Coordinate> active = new HashSet<>();
        Set<Coordinate> inactive = new HashSet<>();
        char[][] grid = ParsingUtils.toGrid(inputString);

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '#') {
                    active.add(new Coordinate(row, column));
                } else {
                    inactive.add(new Coordinate(row, column));
                }
            }
        }

        return new Tuple<>(Collections.unmodifiableSet(active), Collections.unmodifiableSet(inactive));
    }

    private void applyAlgorithm(Set<Coordinate> active, Set<Coordinate> inactive, List<Boolean> algorithm, boolean defaultState) {
        Set<Coordinate> combinedCoordinates = SetUtils.union(active, inactive);

        int minRow = combinedCoordinates.stream().mapToInt(Coordinate::Row).min().orElseThrow() - 1;
        int maxRow = combinedCoordinates.stream().mapToInt(Coordinate::Row).max().orElseThrow() + 1;
        int minColumn = combinedCoordinates.stream().mapToInt(Coordinate::Column).min().orElseThrow() - 1;
        int maxColumn = combinedCoordinates.stream().mapToInt(Coordinate::Column).max().orElseThrow() + 1;

        Set<Coordinate> newActive = new HashSet<>();
        Set<Coordinate> newInactive = new HashSet<>();

        for (int row = minRow; row <= maxRow; row++) {
            for (int column = minColumn; column <= maxColumn; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                List<Coordinate> coordsToConsider = List.of(
                        coordinate.up().left(),
                        coordinate.up(),
                        coordinate.up().right(),
                        coordinate.left(),
                        coordinate,
                        coordinate.right(),
                        coordinate.down().left(),
                        coordinate.down(),
                        coordinate.down().right()
                );

                int targetIndex = 0;

                for (int i = 0; i < coordsToConsider.size(); i++) {
                    Coordinate considered = coordsToConsider.get(i);
                    boolean isActive;
                    if (active.contains(considered)) {
                        isActive = true;
                    } else if (inactive.contains(considered)) {
                        isActive = false;
                    } else {
                        isActive = defaultState;
                    }

                    if (isActive) {
                        targetIndex += (int) Math.pow(2, coordsToConsider.size() - i - 1);
                    }
                }

                if (algorithm.get(targetIndex)) {
                    newActive.add(coordinate);
                } else {
                    newInactive.add(coordinate);
                }
            }
        }

        active.clear();
        active.addAll(newActive);
        inactive.clear();
        inactive.addAll(newInactive);
    }
}
