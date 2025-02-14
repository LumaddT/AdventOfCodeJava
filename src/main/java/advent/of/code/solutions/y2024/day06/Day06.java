package advent.of.code.solutions.y2024.day06;

import advent.of.code.solutions.Day;
import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day06 implements Day {
    @Override
    public String part1(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);
        Tiles[][] grid = Arrays.stream(input)
                .map(l -> Arrays.stream(l)
                        .map(this::charToTile)
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);
        Coordinate initialCoord = MatrixUtils.coordinateOf(grid, Tiles.INITIAL);
        if (initialCoord == null) {
            throw new RuntimeException("Initial coord could not be found.");
        }
        Directions initialDirections = ParsingUtils.charToDirection(MatrixUtils.getMatrixCoord(input, initialCoord));
        MatrixUtils.setMatrixCoord(grid, initialCoord, Tiles.EMPTY);

        return String.format("%d", findTilesVisited(grid, initialCoord, initialDirections).size());
    }

    @Override
    public String part2(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);
        Tiles[][] grid = Arrays.stream(input)
                .map(l -> Arrays.stream(l)
                        .map(this::charToTile)
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);
        Coordinate initialCoord = MatrixUtils.coordinateOf(grid, Tiles.INITIAL);
        if (initialCoord == null) {
            throw new RuntimeException("Initial coord could not be found.");
        }
        Directions initialDirections = ParsingUtils.charToDirection(MatrixUtils.getMatrixCoord(input, initialCoord));
        MatrixUtils.setMatrixCoord(grid, initialCoord, Tiles.EMPTY);

        Set<Coordinate> visited = findTilesVisited(grid, initialCoord, initialDirections);
        int total = 0;

        for (Coordinate coordinate : visited) {
            MatrixUtils.setMatrixCoord(grid, coordinate, Tiles.WALL);
            if (isLoop(grid, initialCoord, initialDirections)) {
                total++;
            }
            MatrixUtils.setMatrixCoord(grid, coordinate, Tiles.EMPTY);
        }

        return String.format("%d", total);
    }

    private Set<Coordinate> findTilesVisited(Tiles[][] grid, Coordinate currentCoord, Directions currentDirections) {
        Set<Coordinate> visited = new HashSet<>();

        while (currentCoord.Row() >= 0 && currentCoord.Row() < grid.length && currentCoord.Column() >= 0 && currentCoord.Column() < grid[0].length) {
            visited.add(currentCoord);
            currentCoord = currentCoord.move(currentDirections);
            while (isWallAhead(grid, currentCoord, currentDirections)) {
                currentDirections = DirectionUtils.rotateClockwise(currentDirections);
            }
        }

        return Collections.unmodifiableSet(visited);
    }

    private boolean isWallAhead(Tiles[][] grid, Coordinate coordinate, Directions direction) {
        coordinate = coordinate.move(direction);
        if (MatrixUtils.isCoordInRange(grid, coordinate)) {
            return MatrixUtils.getMatrixCoord(grid, coordinate) == Tiles.WALL;
        }

        return false;
    }

    private boolean isLoop(Tiles[][] grid, Coordinate currentCoord, Directions currentDirection) {
        Set<CoordinateDirection> visited = new HashSet<>();

        while (MatrixUtils.isCoordInRange(grid, currentCoord)) {
            CoordinateDirection coordinateDirection = new CoordinateDirection(currentCoord, currentDirection);
            if (visited.contains(coordinateDirection)) {
                return true;
            }

            visited.add(coordinateDirection);
            currentCoord = currentCoord.move(currentDirection);
            while (isWallAhead(grid, currentCoord, currentDirection)) {
                currentDirection = DirectionUtils.rotateClockwise(currentDirection);
            }
        }

        return false;
    }

    private Tiles charToTile(char ch) {
        return switch (ch) {
            case '#' -> Tiles.WALL;
            case '.' -> Tiles.EMPTY;
            case '^', 'V', '>', '<' -> Tiles.INITIAL;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }
}
