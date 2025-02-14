package advent.of.code.solutions.y2024.day16;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Quite hacky, especially on part 2, but it works efficiently enough.
 */
@SuppressWarnings("unused")
public class Day16 implements Day {
    private final static char START_CHARACTER = 'S';
    private final static char END_CHARACTER = 'E';

    private final static Directions INITIAL_DIRECTION = Directions.RIGHT;

    private static final int TURNING_COST = 1_000;

    @Override
    public String part1(String inputString) {
        Tiles[][] grid = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .mapToObj(ch -> charToTile((char) ch))
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);

        Coordinate start = MatrixUtils.coordinateOf(grid, Tiles.START);
        Coordinate end = MatrixUtils.coordinateOf(grid, Tiles.END);

        if (start == null || end == null) {
            throw new RuntimeException("Could not find start of end coordinates.");
        }

        Distances[][] distances = calculateDistances(grid, end);

        return String.format("%d", MatrixUtils.getMatrixCoord(distances, start).get(INITIAL_DIRECTION));
    }

    @Override
    public String part2(String inputString) {
        Tiles[][] grid = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .mapToObj(ch -> charToTile((char) ch))
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);

        Coordinate start = MatrixUtils.coordinateOf(grid, Tiles.START);
        Coordinate end = MatrixUtils.coordinateOf(grid, Tiles.END);

        if (start == null || end == null) {
            throw new RuntimeException("Could not find start of end coordinates.");
        }

        Distances[][] distances = calculateDistances(grid, end);

        Distances startDistances = MatrixUtils.getMatrixCoord(distances, start);
        if (startDistances == null) {
            throw new RuntimeException("Bad start coordinate.");
        }

        for (Directions direction : DirectionUtils.ALL_DIRECTIONS) {
            startDistances.addValidFacingDirection(direction);
        }

        Boolean[][] isPartOfIdealPath = new Boolean[distances.length][distances[0].length];
        MatrixUtils.fillMatrix(isPartOfIdealPath, false);
        MatrixUtils.setMatrixCoord(isPartOfIdealPath, start, true);
        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(start);

        while (!toCheck.isEmpty()) {
            Coordinate currentCoordinate = toCheck.poll();

            if (currentCoordinate.equals(new Coordinate(5, 1))) {
                System.out.println("AAA");
            }

            Distances currentDistances = MatrixUtils.getMatrixCoord(distances, currentCoordinate);
            if (currentDistances == null) {
                throw new RuntimeException(String.format("Bad coordinate %s.", currentCoordinate));
            }

            for (Directions facedDirection : currentDistances.getValidFacingDirections()) {
                for (Directions triedDirection : DirectionUtils.ALL_DIRECTIONS) {
                    int currentDistance = currentDistances.get(facedDirection);
                    if (!currentDistances.isValidFacingDirection(triedDirection)) {
                        currentDistance -= TURNING_COST;
                    }

                    Coordinate nextCoordinate = currentCoordinate.move(triedDirection);
                    Distances nextDistances = MatrixUtils.getMatrixCoord(distances, nextCoordinate);
                    if (nextDistances == null) {
                        throw new RuntimeException(String.format("Bad coordinate %s.", nextCoordinate));
                    }

                    if (currentDistance - 1 == nextDistances.get(triedDirection)) {
                        MatrixUtils.setMatrixCoord(isPartOfIdealPath, nextCoordinate, true);
                        nextDistances.addValidFacingDirection(triedDirection);
                        if (!toCheck.contains(nextCoordinate)) {
                            toCheck.add(nextCoordinate);
                        }
                    }
                }
            }
        }

        return String.format("%d", Arrays.stream(isPartOfIdealPath).mapToLong(l -> Arrays.stream(l).filter(e -> e).count()).sum());
    }

    private Tiles charToTile(char ch) {
        return switch (ch) {
            case '#' -> Tiles.WALL;
            case '.' -> Tiles.EMPTY;
            case START_CHARACTER -> Tiles.START;
            case END_CHARACTER -> Tiles.END;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    private Distances[][] calculateDistances(Tiles[][] grid, Coordinate end) {
        Distances[][] distances = new Distances[grid.length][grid[0].length];

        for (int row = 0; row < distances.length; row++) {
            for (int column = 0; column < distances[row].length; column++) {
                distances[row][column] = new Distances(Integer.MAX_VALUE);
            }
        }

        MatrixUtils.setMatrixCoord(distances, end, new Distances(0));
        Queue<Coordinate> toDistribute = new LinkedList<>();
        toDistribute.add(end);

        while (!toDistribute.isEmpty()) {
            Coordinate currentCoordinate = toDistribute.poll();

            for (Directions checkedDirection : DirectionUtils.ALL_DIRECTIONS) {
                Coordinate newCoordinate = currentCoordinate.move(checkedDirection);
                if (MatrixUtils.getMatrixCoord(grid, newCoordinate) == Tiles.WALL) {
                    continue;
                }

                Directions moveDirection = DirectionUtils.opposite(checkedDirection);
                Distances currentDistances = MatrixUtils.getMatrixCoord(distances, currentCoordinate);
                if (currentDistances == null) {
                    throw new RuntimeException(String.format("Bad coordinate %s.", currentCoordinate));
                }
                int tempDistance = currentDistances.get(moveDirection);

                Distances checkedDistances = MatrixUtils.getMatrixCoord(distances, newCoordinate);
                if (checkedDistances == null) {
                    throw new RuntimeException(String.format("Bad Distances found at Coordinate %s.", newCoordinate));
                }

                for (Directions facedDirection : DirectionUtils.ALL_DIRECTIONS) {
                    int turnCost;

                    if (facedDirection == moveDirection) {
                        turnCost = 0;
                    } else if (facedDirection == DirectionUtils.opposite(moveDirection)) {
                        turnCost = TURNING_COST * 2;
                    } else {
                        turnCost = TURNING_COST;
                    }

                    int newDistance = tempDistance + turnCost + 1;
                    int currentDistance = checkedDistances.get(facedDirection);

                    if (newDistance < currentDistance) {
                        checkedDistances.put(facedDirection, newDistance);
                        if (!toDistribute.contains(newCoordinate)) {
                            toDistribute.add(newCoordinate);
                        }
                    }
                }
            }
        }

        return distances;
    }
}
