package advent.of.code.solutions.y2021.day25;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.ntuples.Tuple;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day25 implements Day {
    @Override
    public String part1(String inputString) {
        char[][] grid = ParsingUtils.toGrid(inputString);

        Tuple<Set<Coordinate>, Set<Coordinate>> input = parseGrid(grid);
        Set<Coordinate> east = input.First();
        Set<Coordinate> south = input.Second();

        for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
            boolean moved = false;

            Set<Coordinate> newEast = new HashSet<>();

            for (Coordinate coordinate : east) {
                Coordinate newCoordinate = coordinate.right().columnModulo(grid[0].length);
                if (east.contains(newCoordinate) || south.contains(newCoordinate)) {
                    newEast.add(coordinate);
                } else {
                    newEast.add(newCoordinate);
                    moved = true;
                }
            }

            east = newEast;

            Set<Coordinate> newSouth = new HashSet<>();

            for (Coordinate coordinate : south) {
                Coordinate newCoordinate = coordinate.down().rowModulo(grid.length);
                if (east.contains(newCoordinate) || south.contains(newCoordinate)) {
                    newSouth.add(coordinate);
                } else {
                    newSouth.add(newCoordinate);
                    moved = true;
                }
            }

            south = newSouth;

            if (!moved) {
                return String.format("%d", i + 1);
            }
        }

        throw new RuntimeException("Exceeded iteration limit.");
    }

    @Override
    public String part2(String inputString) {
        return "No part 2 on Christmas.";
    }

    private Tuple<Set<Coordinate>, Set<Coordinate>> parseGrid(char[][] grid) {
        Set<Coordinate> east = new HashSet<>();
        Set<Coordinate> south = new HashSet<>();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '>') {
                    east.add(new Coordinate(row, column));
                } else if (grid[row][column] == 'v') {
                    south.add(new Coordinate(row, column));
                }
            }
        }

        return new Tuple<>(east, south);
    }
}
