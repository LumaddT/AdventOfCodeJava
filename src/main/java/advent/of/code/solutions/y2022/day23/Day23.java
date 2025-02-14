package advent.of.code.solutions.y2022.day23;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;
import advent.of.code.utils.ParsingUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day23 implements Day {
    private final static int ITERATIONS_PART_1 = 10;

    @Override
    public String part1(String inputString) {
        Set<Coordinate> input = parseInputString(inputString);

        List<Directions> directionsToTry = new ArrayList<>();
        directionsToTry.add(Directions.UP);
        directionsToTry.add(Directions.DOWN);
        directionsToTry.add(Directions.LEFT);
        directionsToTry.add(Directions.RIGHT);

        Coordinate[] currentCoordinatesArray = input.toArray(Coordinate[]::new);
        Set<Coordinate> currentCoordinatesSet = new HashSet<>(input);

        for (int iteration = 0; iteration < ITERATIONS_PART_1; iteration++) {
            performRound(currentCoordinatesArray, currentCoordinatesSet, directionsToTry);
        }

        int minRow = currentCoordinatesSet.stream().mapToInt(Coordinate::Row).min().orElseThrow();
        int maxRow = currentCoordinatesSet.stream().mapToInt(Coordinate::Row).max().orElseThrow();
        int minColumn = currentCoordinatesSet.stream().mapToInt(Coordinate::Column).min().orElseThrow();
        int maxColumn = currentCoordinatesSet.stream().mapToInt(Coordinate::Column).max().orElseThrow();

        int rows = maxRow - minRow + 1;
        int columns = maxColumn - minColumn + 1;

        return String.format("%d", rows * columns - currentCoordinatesArray.length);
    }

    @Override
    public String part2(String inputString) {
        Set<Coordinate> input = parseInputString(inputString);

        List<Directions> directionsToTry = new ArrayList<>();
        directionsToTry.add(Directions.UP);
        directionsToTry.add(Directions.DOWN);
        directionsToTry.add(Directions.LEFT);
        directionsToTry.add(Directions.RIGHT);

        Coordinate[] currentCoordinatesArray = input.toArray(Coordinate[]::new);
        Set<Coordinate> currentCoordinatesSet = new HashSet<>(input);

        for (int iteration = 0; iteration < Integer.MAX_VALUE - 1; iteration++) {
            if (!performRound(currentCoordinatesArray, currentCoordinatesSet, directionsToTry)) {
                return String.format("%d", iteration + 1);
            }
        }

        throw new RuntimeException("Exceeded maximum iterations.");
    }

    private Set<Coordinate> parseInputString(String inputString) {
        char[][] grid = ParsingUtils.toGrid(inputString);
        Set<Coordinate> coordinates = new HashSet<>();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '#') {
                    coordinates.add(new Coordinate(row, column));
                }
            }
        }

        return coordinates;
    }

    /**
     * @return `true` if at least one elf moved, otherwise `false`.
     */
    private boolean performRound(Coordinate[] currentCoordinatesArray, Set<Coordinate> currentCoordinatesSet, List<Directions> directionsToTry) {
        Coordinate[] proposedMovementsArray = new Coordinate[currentCoordinatesArray.length];
        Set<Coordinate> proposedMovementsSet = new HashSet<>();

        boolean[] shouldMove = new boolean[currentCoordinatesArray.length];
        Arrays.fill(shouldMove, false);
        boolean atLeastOneMoves = false;

        for (int elf = 0; elf < currentCoordinatesArray.length; elf++) {
            for (Coordinate adjacent : currentCoordinatesArray[elf].adjacent()) {
                if (currentCoordinatesSet.contains(adjacent)) {
                    shouldMove[elf] = true;
                    atLeastOneMoves = true;
                    break;
                }
            }
        }

        if (!atLeastOneMoves) {
            return false;
        }

        for (int elf = 0; elf < currentCoordinatesArray.length; elf++) {
            if (!shouldMove[elf]) {
                continue;
            }

            proposedMovementsArray[elf] = null;
            for (Directions direction : directionsToTry) {
                if (canMove(currentCoordinatesArray[elf], direction, currentCoordinatesSet)) {
                    Coordinate proposal = currentCoordinatesArray[elf].move(direction);
                    if (!proposedMovementsSet.contains(proposal)) {
                        proposedMovementsArray[elf] = proposal;
                        proposedMovementsSet.add(proposal);
                    } else {
                        for (int i = 0; i < elf; i++) {
                            if (proposedMovementsArray[i] != null && proposedMovementsArray[i].equals(proposal)) {
                                proposedMovementsArray[i] = null;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        currentCoordinatesSet.clear();
        for (int elf = 0; elf < currentCoordinatesArray.length; elf++) {
            if (proposedMovementsArray[elf] == null) {
                currentCoordinatesSet.add(currentCoordinatesArray[elf]);
            } else {
                currentCoordinatesSet.add(proposedMovementsArray[elf]);
                currentCoordinatesArray[elf] = proposedMovementsArray[elf];
            }
        }

        Directions checkedDirection = directionsToTry.getFirst();
        directionsToTry.removeFirst();
        directionsToTry.add(checkedDirection);

        return true;
    }

    private boolean canMove(Coordinate coordinate, Directions direction, Set<Coordinate> currentCoordinatesSet) {
        if (currentCoordinatesSet.contains(coordinate.move(direction))) {
            return false;
        }

        for (Directions perpendicularDirection : DirectionUtils.perpendicularDirections(direction)) {
            if (currentCoordinatesSet.contains(coordinate.move(direction).move(perpendicularDirection))) {
                return false;
            }
        }

        return true;
    }
}