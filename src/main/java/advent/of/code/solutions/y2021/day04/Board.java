package advent.of.code.solutions.y2021.day04;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private final Integer[][] Grid;
    @Getter
    private Integer NumbersToWin = null;
    private final Set<Coordinate> Marked = new HashSet<>();
    @Getter
    private Integer WinnerNumber = null;

    public Board(Integer[][] grid) {
        Grid = grid;
    }

    public void playBingo(int[] numbers) {
        if (NumbersToWin != null) {
            return;
        }

        for (int i = 0; i < numbers.length; i++) {
            markNumber(numbers[i]);
            if (isWon()) {
                NumbersToWin = i;
                WinnerNumber = numbers[i];
                return;
            }
        }

        throw new RuntimeException("Did not win.");
    }

    public void markNumber(int value) {
        Coordinate coordinate = MatrixUtils.coordinateOf(Grid, value);
        if (coordinate != null) {
            Marked.add(coordinate);
        }
    }

    public boolean isWon() {
        for (int row = 0; row < Grid.length; row++) {
            boolean isWon = true;

            for (int column = 0; column < Grid[row].length; column++) {
                if (!Marked.contains(Coordinate.ORIGIN.down(row).right(column))) {
                    isWon = false;
                    break;
                }
            }

            if (isWon) {
                return true;
            }
        }

        for (int column = 0; column < Grid[0].length; column++) {
            boolean hasWon = true;

            for (int row = 0; row < Grid.length; row++) {
                if (!Marked.contains(new Coordinate(row, column))) {
                    hasWon = false;
                    break;
                }
            }

            if (hasWon) {
                return true;
            }
        }

        return false;
    }

    public int getSumOfUnmarkedNumbers() {
        int total = 0;

        for (int row = 0; row < Grid.length; row++) {
            for (int column = 0; column < Grid[row].length; column++) {
                if (!Marked.contains(new Coordinate(row, column))) {
                    total += Grid[row][column];
                }
            }
        }

        return total;
    }
}
