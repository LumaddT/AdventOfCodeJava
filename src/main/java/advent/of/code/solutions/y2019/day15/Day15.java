package advent.of.code.solutions.y2019.day15;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;

import java.util.*;

@SuppressWarnings("unused")
public class Day15 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        Grid grid = generateGrid(program);

        return String.format("%d", distanceToTarget(grid));
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        Grid grid = generateGrid(program);

        return String.format("%d", timeToFill(grid));
    }

    private Grid generateGrid(IntcodeProgram program) {
        Map<Coordinate, Tiles> gridMap = generateGridMap(program);

        int minRow = gridMap.keySet().stream().mapToInt(Coordinate::Row).min().orElseThrow();
        int minColumn = gridMap.keySet().stream().mapToInt(Coordinate::Column).min().orElseThrow();
        int maxRow = gridMap.keySet().stream().mapToInt(Coordinate::Row).max().orElseThrow();
        int maxColumn = gridMap.keySet().stream().mapToInt(Coordinate::Column).max().orElseThrow();

        int gridRows = maxRow - minRow + 1;
        int gridColumns = maxColumn - minColumn + 1;

        Coordinate origin = new Coordinate(-minRow, -minColumn);

        Tiles[][] gridArray = new Tiles[gridRows][gridColumns];
        MatrixUtils.fillMatrix(gridArray, null);

        Coordinate target = null;

        for (Coordinate coordinate : gridMap.keySet()) {
            MatrixUtils.setMatrixCoord(gridArray, coordinate.add(origin), gridMap.get(coordinate));

            if (gridMap.get(coordinate) == Tiles.TARGET) {
                if (target != null) {
                    throw new RuntimeException("Bad grid generation.");
                }

                target = coordinate.add(origin);
            }
        }

        if (target == null) {
            throw new RuntimeException("Bad grid generation.");
        }

        return new Grid(gridArray, origin, target);
    }

    private Map<Coordinate, Tiles> generateGridMap(IntcodeProgram program) {
        Map<Coordinate, Tiles> gridMap = new HashMap<>();

        Coordinate currentCoord = Coordinate.ORIGIN;
        gridMap.put(currentCoord, Tiles.FLOOR);

        Directions lastDirectionMoved = Directions.UP;

        do {
            List<Directions> directionsToTry = getDirectionsToTry(lastDirectionMoved);
            Coordinate oldCoord = currentCoord;
            boolean foundNextMovement = false;

            for (Directions directionToTry : directionsToTry) {
                currentCoord = oldCoord.move(directionToTry);
                Tiles output = executeMovement(directionToTry, program);
                gridMap.putIfAbsent(currentCoord, output);

                if (output != Tiles.WALL) {
                    if (!foundNextMovement) {
                        lastDirectionMoved = directionToTry;
                        foundNextMovement = true;
                    }

                    executeMovement(DirectionUtils.opposite(directionToTry), program);
                }
            }

            currentCoord = oldCoord.move(lastDirectionMoved);
            executeMovement(lastDirectionMoved, program);
        } while (!(currentCoord.equals(Coordinate.ORIGIN) && lastDirectionMoved == Directions.UP));

        return gridMap;
    }

    private int distanceToTarget(Grid grid) {
        Integer[][] distances = new Integer[grid.GridArray().length][grid.GridArray()[0].length];
        MatrixUtils.fillMatrix(distances, Integer.MAX_VALUE);
        MatrixUtils.setMatrixCoord(distances, grid.Origin(), 0);

        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(grid.Origin());

        while (!toCheck.isEmpty()) {
            Coordinate checkedCoordinate = toCheck.poll();
            int newDistance = MatrixUtils.getMatrixCoord(distances, checkedCoordinate) + 1;

            for (Coordinate nextCoordinate : checkedCoordinate.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(grid.GridArray(), nextCoordinate) == Tiles.TARGET) {
                    return newDistance;
                }

                if (MatrixUtils.getMatrixCoord(grid.GridArray(), nextCoordinate) != Tiles.WALL) {
                    if (newDistance < MatrixUtils.getMatrixCoord(distances, nextCoordinate)) {
                        MatrixUtils.setMatrixCoord(distances, nextCoordinate, newDistance);
                        toCheck.add(nextCoordinate);
                    }
                }
            }
        }

        throw new RuntimeException("Bad distance to target method.");
    }

    private int timeToFill(Grid grid) {
        Integer[][] distances = new Integer[grid.GridArray().length][grid.GridArray()[0].length];
        MatrixUtils.fillMatrix(distances, Integer.MAX_VALUE);
        MatrixUtils.setMatrixCoord(distances, grid.Target(), 0);

        Queue<Coordinate> toCheck = new ArrayDeque<>();
        toCheck.add(grid.Target());

        while (!toCheck.isEmpty()) {
            Coordinate checkedCoordinate = toCheck.poll();
            int newDistance = MatrixUtils.getMatrixCoord(distances, checkedCoordinate) + 1;

            for (Coordinate nextCoordinate : checkedCoordinate.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(grid.GridArray(), nextCoordinate) != Tiles.WALL) {
                    if (newDistance < MatrixUtils.getMatrixCoord(distances, nextCoordinate)) {
                        MatrixUtils.setMatrixCoord(distances, nextCoordinate, newDistance);
                        toCheck.add(nextCoordinate);
                    }
                }
            }
        }

        return Arrays.stream(distances)
                .mapToInt(l -> Arrays.stream(l)
                        .mapToInt(v -> v)
                        .filter(v -> v != Integer.MAX_VALUE)
                        .max()
                        .orElse(Integer.MAX_VALUE))
                .filter(v -> v != Integer.MAX_VALUE)
                .max()
                .orElseThrow();
    }

    private Tiles executeMovement(Directions direction, IntcodeProgram program) {
        program.setInput(directionToInput(direction));

        program.executeAll();

        return switch (program.readOutputAsInt()) {
            case 0 -> Tiles.WALL;
            case 1 -> Tiles.FLOOR;
            case 2 -> Tiles.TARGET;
            default -> throw new RuntimeException("Bad output");
        };
    }

    private int directionToInput(Directions direction) {
        return switch (direction) {
            case UP -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
            case RIGHT -> 4;
        };
    }

    private List<Directions> getDirectionsToTry(Directions direction) {
        return switch (direction) {
            case UP -> List.of(Directions.RIGHT, Directions.UP, Directions.LEFT, Directions.DOWN);
            case RIGHT -> List.of(Directions.DOWN, Directions.RIGHT, Directions.UP, Directions.LEFT);
            case DOWN -> List.of(Directions.LEFT, Directions.DOWN, Directions.RIGHT, Directions.UP);
            case LEFT -> List.of(Directions.UP, Directions.LEFT, Directions.DOWN, Directions.RIGHT);
        };
    }
}
