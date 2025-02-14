package advent.of.code.solutions.y2024.day20;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@SuppressWarnings("unused")
public class Day20 implements Day {
    private final static char START_CHARACTER = 'S';
    private final static char END_CHARACTER = 'E';

    private final static int CHEAT_PART_1 = 2;
    private final static int CHEAT_PART_2 = 20;

    private final static int TIME_SAVE_CUTOFF = 100;

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

        Integer[][] distancesFromStart = calculateDistances(grid, start);
        Integer[][] distancesToEnd = calculateDistances(grid, end);
        int timeNoCheats = MatrixUtils.getMatrixCoord(distancesFromStart, end);

        if (timeNoCheats != MatrixUtils.getMatrixCoord(distancesToEnd, start)) {
            throw new RuntimeException("Bad no cheat time calculation.");
        }

        int cheatsThatSaveTime = countCheatsThatSaveTime(grid, distancesFromStart, distancesToEnd, timeNoCheats, CHEAT_PART_1);

        return String.format("%d", cheatsThatSaveTime);
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

        Integer[][] distancesFromStart = calculateDistances(grid, start);
        Integer[][] distancesToEnd = calculateDistances(grid, end);
        int timeNoCheats = MatrixUtils.getMatrixCoord(distancesFromStart, end);

        if (timeNoCheats != MatrixUtils.getMatrixCoord(distancesToEnd, start)) {
            throw new RuntimeException("Bad no cheat time calculation.");
        }

        int cheatsThatSaveTime = countCheatsThatSaveTime(grid, distancesFromStart, distancesToEnd, timeNoCheats, CHEAT_PART_2);

        return String.format("%d", cheatsThatSaveTime);
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

    private Integer[][] calculateDistances(Tiles[][] grid, Coordinate origin) {
        Integer[][] distances = new Integer[grid.length][grid[0].length];
        MatrixUtils.fillMatrix(distances, Integer.MAX_VALUE);
        MatrixUtils.setMatrixCoord(distances, origin, 0);

        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(origin);

        while (!toCheck.isEmpty()) {
            Coordinate currentCoordinate = toCheck.poll();
            int newValue = MatrixUtils.getMatrixCoord(distances, currentCoordinate) + 1;

            for (Coordinate adjacent : currentCoordinate.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(grid, adjacent) != Tiles.WALL) {
                    int oldValue = MatrixUtils.getMatrixCoord(distances, adjacent);
                    if (newValue < oldValue) {
                        MatrixUtils.setMatrixCoord(distances, adjacent, newValue);
                        toCheck.add(adjacent);
                    }
                }
            }
        }

        return distances;
    }

    private int countCheatsThatSaveTime(Tiles[][] grid, Integer[][] distancesFromStart, Integer[][] distancesToEnd, int timeNoCheats, int cheatLength) {
        if (distancesFromStart.length != distancesToEnd.length || distancesFromStart[0].length != distancesToEnd[0].length) {
            throw new RuntimeException("Bad distances tables lengths.");
        }

        int total = 0;

        for (int row = 0; row < distancesFromStart.length; row++) {
            for (int column = 0; column < distancesFromStart[row].length; column++) {
                Coordinate coordinate = new Coordinate(row, column);

                if (MatrixUtils.getMatrixCoord(grid, coordinate) == Tiles.WALL) {
                    continue;
                }

                Set<Coordinate> targetsInRadius = coordinate.coordinatesInRadius(cheatLength);
                for (Coordinate targetCoordinate : targetsInRadius) {
                    if (!MatrixUtils.isCoordInRange(grid, targetCoordinate) || MatrixUtils.getMatrixCoord(grid, targetCoordinate) == Tiles.WALL) {
                        continue;
                    }

                    if (savesTime(distancesFromStart, distancesToEnd, coordinate, targetCoordinate, timeNoCheats)) {
                        total++;
                    }
                }
            }
        }

        return total;
    }

    private boolean savesTime(Integer[][] distancesFromStart, Integer[][] distancesToEnd, Coordinate currentCoordinate, Coordinate targetCoordinate, int timeNoCheats) {
        int distanceFromStartBeforeCheat = MatrixUtils.getMatrixCoord(distancesFromStart, currentCoordinate);
        int distanceToEndAfterCheat = MatrixUtils.getMatrixCoord(distancesToEnd, targetCoordinate);

        int timeWithCheat = distanceFromStartBeforeCheat + distanceToEndAfterCheat + currentCoordinate.manhattanDistance(targetCoordinate);

        return timeNoCheats - timeWithCheat >= TIME_SAVE_CUTOFF;
    }
}
