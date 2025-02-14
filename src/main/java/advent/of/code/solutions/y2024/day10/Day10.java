package advent.of.code.solutions.y2024.day10;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day10 implements Day {
    @Override
    public String part1(String inputString) {
        Integer[][] input = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .map(c -> c - '0')
                        .boxed()
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        int total = 0;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (input[row][column] == 0) {
                    total += trailheadScore(input, new Coordinate(row, column), new HashSet<>());
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Integer[][] input = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .map(c -> c - '0')
                        .boxed()
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        int total = 0;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (input[row][column] == 0) {
                    total += trailheadScore(input, new Coordinate(row, column), null);
                }
            }
        }

        return String.format("%d", total);
    }

    private int trailheadScore(Integer[][] input, Coordinate coordinate, Set<Coordinate> visited) {
        if (visited != null) {
            if (visited.contains(coordinate)) {
                return 0;
            }
            visited.add(coordinate);
        }

        int returnValue = 0;
        int currentHeight = MatrixUtils.getMatrixCoord(input, coordinate);

        if (currentHeight == 9) {
            return 1;
        }

        for (Coordinate adjacent : coordinate.orthogonallyAdjacent()) {
            if (MatrixUtils.isCoordInRange(input, adjacent)) {
                int adjacentHeight = MatrixUtils.getMatrixCoord(input, adjacent);
                if (adjacentHeight - currentHeight == 1) {
                    returnValue += trailheadScore(input, adjacent, visited);
                }
            }
        }

        return returnValue;
    }
}
