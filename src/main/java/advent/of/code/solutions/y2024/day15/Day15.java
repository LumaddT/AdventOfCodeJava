package advent.of.code.solutions.y2024.day15;

import advent.of.code.solutions.Day;
import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Day15 implements Day {
    @Override
    public String part1(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString.split("\n\n")[0]);
        Directions[] path = inputString.split("\n\n")[1].replace("\n", "").chars()
                .mapToObj(c -> ParsingUtils.charToDirection((char) c))
                .toArray(Directions[]::new);

        Coordinate initialCoord = MatrixUtils.coordinateOf(input, '@');
        if (initialCoord == null) {
            throw new RuntimeException("No position character");
        }

        Tiles[][] grid = inputToGrid(input);
        Coordinate currentCoord = initialCoord;

        for (Directions direction : path) {
            Coordinate newCoord = currentCoord.move(direction);

            if (MatrixUtils.getMatrixCoord(grid, newCoord) == Tiles.WALL) {
                continue;
            }

            if (MatrixUtils.getMatrixCoord(grid, newCoord) == Tiles.BOX) {
                if (!moveBoxes(newCoord, direction, grid)) {
                    continue;
                }
            }

            currentCoord = newCoord;
        }

        int total = 0;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (grid[row][column] == Tiles.BOX) {
                    total += row * 100 + column;
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString.split("\n\n")[0]);
        Directions[] path = inputString.split("\n\n")[1].replace("\n", "").chars()
                .mapToObj(c -> ParsingUtils.charToDirection((char) c))
                .toArray(Directions[]::new);

        Coordinate initialCoord = MatrixUtils.coordinateOf(input, '@');
        if (initialCoord == null) {
            throw new RuntimeException("No position character");
        }
        initialCoord = new Coordinate(initialCoord.Row(), initialCoord.Column() * 2);

        Tiles[][] grid = inputToGridDouble(input);
        Coordinate currentCoord = initialCoord;

        for (Directions direction : path) {
            Coordinate newCoord = currentCoord.move(direction);

            if (MatrixUtils.getMatrixCoord(grid, newCoord) == Tiles.WALL) {
                continue;
            }

            if (MatrixUtils.getMatrixCoord(grid, newCoord) == Tiles.BOX_LEFT || MatrixUtils.getMatrixCoord(grid, newCoord) == Tiles.BOX_RIGHT) {
                if (!moveBoxesDouble(newCoord, direction, grid)) {
                    continue;
                }
            }

            currentCoord = newCoord;
        }

        int total = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == Tiles.BOX_LEFT) {
                    total += row * 100 + column;
                }
            }
        }

        return String.format("%d", total);
    }

    private Tiles[][] inputToGrid(Character[][] input) {
        return Arrays.stream(input)
                .map(l -> Arrays.stream(l)
                        .map(this::charToTile)
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);
    }

    private Tiles charToTile(char ch) {
        return switch (ch) {
            case '#' -> Tiles.WALL;
            case 'O' -> Tiles.BOX;
            case '.', '@' -> Tiles.EMPTY;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    private Tiles[][] inputToGridDouble(Character[][] input) {
        Tiles[][] grid = new Tiles[input.length][input[0].length * 2];

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                switch (input[row][column]) {
                    case '#' -> {
                        grid[row][column * 2] = Tiles.WALL;
                        grid[row][column * 2 + 1] = Tiles.WALL;
                    }
                    case 'O' -> {
                        grid[row][column * 2] = Tiles.BOX_LEFT;
                        grid[row][column * 2 + 1] = Tiles.BOX_RIGHT;
                    }
                    case '.', '@' -> {
                        grid[row][column * 2] = Tiles.EMPTY;
                        grid[row][column * 2 + 1] = Tiles.EMPTY;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + input[row][column]);
                }
            }
        }

        return grid;
    }

    private boolean moveBoxes(Coordinate firstBoxCoord, Directions direction, Tiles[][] grid) {
        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) != Tiles.BOX) {
            throw new RuntimeException("Bad call to moveBoxes");
        }

        Coordinate currentCoord = firstBoxCoord;
        while (MatrixUtils.getMatrixCoord(grid, currentCoord) == Tiles.BOX) {
            currentCoord = currentCoord.move(direction);
        }

        if (MatrixUtils.getMatrixCoord(grid, currentCoord) == Tiles.WALL) {
            return false;
        }

        MatrixUtils.setMatrixCoord(grid, currentCoord, Tiles.BOX);
        MatrixUtils.setMatrixCoord(grid, firstBoxCoord, Tiles.EMPTY);

        return true;
    }

    private boolean moveBoxesDouble(Coordinate firstBoxCoord, Directions direction, Tiles[][] grid) {
        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) != Tiles.BOX_LEFT && MatrixUtils.getMatrixCoord(grid, firstBoxCoord) != Tiles.BOX_RIGHT) {
            throw new RuntimeException("Bad call to moveBoxes");
        }

        if (!canMove(firstBoxCoord, direction, grid)) {
            return false;
        }

        performMoveBoxesDouble(firstBoxCoord, direction, grid);

        return true;
    }

    private boolean canMove(Coordinate firstBoxCoord, Directions direction, Tiles[][] grid) {
        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) == Tiles.EMPTY) {
            return true;
        }

        if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            Coordinate nextCoord = firstBoxCoord.move(direction).move(direction);
            if (MatrixUtils.getMatrixCoord(grid, nextCoord) == Tiles.WALL) {
                return false;
            }
            return canMove(firstBoxCoord.move(direction).move(direction), direction, grid);
        }

        Coordinate nextCoordLeft;
        Coordinate nextCoordRight;

        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) == Tiles.BOX_LEFT) {
            nextCoordLeft = firstBoxCoord.move(direction);
            nextCoordRight = firstBoxCoord.move(direction).right();
        } else {
            nextCoordRight = firstBoxCoord.move(direction);
            nextCoordLeft = firstBoxCoord.move(direction).left();
        }

        if (MatrixUtils.getMatrixCoord(grid, nextCoordLeft) == Tiles.WALL || MatrixUtils.getMatrixCoord(grid, nextCoordRight) == Tiles.WALL) {
            return false;
        }

        return canMove(nextCoordLeft, direction, grid) && canMove(nextCoordRight, direction, grid);
    }

    private void performMoveBoxesDouble(Coordinate firstBoxCoord, Directions direction, Tiles[][] grid) {
        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) == Tiles.EMPTY) {
            return;
        }

        if (direction == Directions.LEFT || direction == Directions.RIGHT) {
            Coordinate nextCoord = firstBoxCoord.move(direction);
            performMoveBoxesDouble(nextCoord, direction, grid);
            MatrixUtils.setMatrixCoord(grid, nextCoord, MatrixUtils.getMatrixCoord(grid, firstBoxCoord));
            MatrixUtils.setMatrixCoord(grid, firstBoxCoord, Tiles.EMPTY);
            return;
        }

        Coordinate nextCoordLeft;
        Coordinate nextCoordRight;

        if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) == Tiles.BOX_LEFT) {
            nextCoordLeft = firstBoxCoord.move(direction);
            nextCoordRight = firstBoxCoord.move(direction).right();
        } else if (MatrixUtils.getMatrixCoord(grid, firstBoxCoord) == Tiles.BOX_RIGHT) {
            nextCoordRight = firstBoxCoord.move(direction);
            nextCoordLeft = firstBoxCoord.move(direction).left();
        } else {
            throw new IllegalStateException("Illegal pushing.");
        }

        performMoveBoxesDouble(nextCoordLeft, direction, grid);
        performMoveBoxesDouble(nextCoordRight, direction, grid);

        MatrixUtils.setMatrixCoord(grid, nextCoordLeft, Tiles.BOX_LEFT);
        MatrixUtils.setMatrixCoord(grid, nextCoordRight, Tiles.BOX_RIGHT);
        MatrixUtils.setMatrixCoord(grid, nextCoordLeft.move(DirectionUtils.opposite(direction)), Tiles.EMPTY);
        MatrixUtils.setMatrixCoord(grid, nextCoordRight.move(DirectionUtils.opposite(direction)), Tiles.EMPTY);
    }
}
