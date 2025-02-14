package advent.of.code.solutions.y2024.day04;

import advent.of.code.solutions.Day;
import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;

@SuppressWarnings("unused")
public class Day04 implements Day {
    @Override
    public String part1(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);

        int total = 0;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                total += countXmas(new Coordinate(row, column), input);
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);

        int total = 0;

        for (int row = 1; row < input.length - 1; row++) {
            for (int column = 1; column < input[row].length - 1; column++) {
                if (isX_Mas(new Coordinate(row, column), input)) {
                    total++;
                }
            }
        }

        return String.format("%d", total);
    }

    private int countXmas(Coordinate coordinate, Character[][] grid) {
        int total = 0;

        for (Directions direction : DirectionUtils.ALL_DIRECTIONS) {
            if (MatrixUtils.isCoordInRange(grid, coordinate.move(direction, 3))
                    && MatrixUtils.getMatrixCoord(grid, coordinate.move(direction, 0)) == 'X'
                    && MatrixUtils.getMatrixCoord(grid, coordinate.move(direction, 1)) == 'M'
                    && MatrixUtils.getMatrixCoord(grid, coordinate.move(direction, 2)) == 'A'
                    && MatrixUtils.getMatrixCoord(grid, coordinate.move(direction, 3)) == 'S') {
                total++;
            }
        }

        for (Directions verticalDirection : DirectionUtils.VERTICAL_DIRECTIONS) {
            for (Directions horizontalDirection : DirectionUtils.HORIZONTAL_DIRECTIONS) {
                if (MatrixUtils.isCoordInRange(grid, coordinate.move(verticalDirection, 3).move(horizontalDirection, 3))
                        && MatrixUtils.getMatrixCoord(grid, coordinate.move(verticalDirection, 0).move(horizontalDirection, 0)) == 'X'
                        && MatrixUtils.getMatrixCoord(grid, coordinate.move(verticalDirection, 1).move(horizontalDirection, 1)) == 'M'
                        && MatrixUtils.getMatrixCoord(grid, coordinate.move(verticalDirection, 2).move(horizontalDirection, 2)) == 'A'
                        && MatrixUtils.getMatrixCoord(grid, coordinate.move(verticalDirection, 3).move(horizontalDirection, 3)) == 'S') {
                    total++;
                }
            }
        }

        return total;
    }

    private boolean isX_Mas(Coordinate coordinate, Character[][] grid) {
        if (MatrixUtils.getMatrixCoord(grid, coordinate) != 'A') {
            return false;
        }

        int masCount = 0;

        for (Directions verticalDirection : DirectionUtils.VERTICAL_DIRECTIONS) {
            for (Directions horizontalDirection : DirectionUtils.HORIZONTAL_DIRECTIONS) {
                if (MatrixUtils.getMatrixCoord(grid, coordinate.move(horizontalDirection).move(verticalDirection)) == 'M'
                        && MatrixUtils.getMatrixCoord(grid, coordinate.move(DirectionUtils.opposite(horizontalDirection)).move(DirectionUtils.opposite(verticalDirection))) == 'S') {
                    masCount++;
                }
            }
        }

        return masCount == 2;
    }
}
