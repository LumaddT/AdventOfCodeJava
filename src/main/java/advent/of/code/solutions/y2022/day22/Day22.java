package advent.of.code.solutions.y2022.day22;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Day22 implements Day {
    private static final Directions INITIAL_DIRECTION = Directions.RIGHT;

    private static final int CUBE_SIDE_LENGTH = 50;

    @Override
    public String part1(String inputString) {
        Tiles[][] grid = MatrixUtils.addFrame(parseGrid(inputString), Tiles.NULL);
        Instruction[] instructions = parseInstructions(inputString);

        Coordinate currentCoordinate = MatrixUtils.coordinateOf(grid, Tiles.EMPTY);
        if (currentCoordinate == null) {
            throw new RuntimeException("Could not find initial coordinate.");
        }
        Directions currentDirection = INITIAL_DIRECTION;

        for (Instruction instruction : instructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                Coordinate previousCoordinate = currentCoordinate;

                currentCoordinate = currentCoordinate.move(currentDirection);

                if (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == Tiles.WALL) {
                    currentCoordinate = previousCoordinate;
                    break;
                }

                if (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == Tiles.NULL) {
                    currentCoordinate = switch (currentDirection) {
                        case UP -> currentCoordinate.setRow(grid.length - 1);
                        case DOWN -> currentCoordinate.topRow();
                        case LEFT -> currentCoordinate.setColumn(grid[currentCoordinate.Row()].length - 1);
                        case RIGHT -> currentCoordinate.leftmostColumn();
                    };

                    while (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == Tiles.NULL) {
                        currentCoordinate = currentCoordinate.move(currentDirection);
                    }

                    if (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == Tiles.WALL) {
                        currentCoordinate = previousCoordinate;
                        break;
                    }
                }
            }

            currentDirection = switch (instruction.getRotation()) {
                case LEFT -> DirectionUtils.rotateCounterclockwise(currentDirection);
                case RIGHT -> DirectionUtils.rotateClockwise(currentDirection);
                case null -> currentDirection;
                case UP, DOWN -> throw new RuntimeException("Illegal instruction rotation.");
            };
        }

        return String.format("%d", 1000 * currentCoordinate.Row() + 4 * currentCoordinate.Column() + directionToValue(currentDirection));
    }

    /**
     * The solution is very input specific, but it was much faster to write it this way.
     * Other than forgetting to add 1 to the row and column in the return, I got the solution
     * right on the first try. It was very surprising.
     */
    @Override
    public String part2(String inputString) {
        Instruction[] instructions = parseInstructions(inputString);

        Side currentSide = parseGrids(inputString);
        Coordinate currentCoordinate = Coordinate.ORIGIN;
        Directions currentDirection = INITIAL_DIRECTION;

        for (Instruction instruction : instructions) {
            for (int i = 0; i < instruction.getAmount(); i++) {
                Coordinate previousCoordinate = currentCoordinate;
                Side previousSide = currentSide;
                Directions previousDirection = currentDirection;

                currentCoordinate = currentCoordinate.move(currentDirection);

                if (!currentSide.isCoordInRange(currentCoordinate)) {
                    currentSide = previousSide.getSide(previousDirection);
                    currentDirection = previousSide.getNewDirection(previousDirection);

                    currentCoordinate = calculateNewCoordinate(currentCoordinate.move(DirectionUtils.opposite(previousDirection)), previousDirection, currentDirection);

                    if (currentSide.isWall(currentCoordinate)) {
                        currentSide = previousSide;
                        currentDirection = previousDirection;
                        currentCoordinate = previousCoordinate;
                        break;
                    }
                } else if (currentSide.isWall(currentCoordinate)) {
                    currentCoordinate = previousCoordinate;
                    break;
                }
            }

            currentDirection = switch (instruction.getRotation()) {
                case LEFT -> DirectionUtils.rotateCounterclockwise(currentDirection);
                case RIGHT -> DirectionUtils.rotateClockwise(currentDirection);
                case null -> currentDirection;
                case UP, DOWN -> throw new RuntimeException("Illegal instruction rotation.");
            };
        }

        return String.format("%d", 1000 * (currentCoordinate.Row() + currentSide.getTopLeftCorner().Row() + 1) + 4 * (currentCoordinate.Column() + currentSide.getTopLeftCorner().Column() + 1) + directionToValue(currentDirection));
    }

    private Tiles[][] parseGrid(String inputString) {
        return Arrays.stream(inputString.split("\n\n")[0].split("\n"))
                .map(l -> String.format("%-150s", l).chars()
                        .mapToObj(ch -> charToTile((char) ch))
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);
    }

    private Tiles charToTile(char ch) {
        return switch (ch) {
            case '#' -> Tiles.WALL;
            case '.' -> Tiles.EMPTY;
            case ' ' -> Tiles.NULL;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    private Instruction[] parseInstructions(String inputString) {
        return Arrays.stream(inputString.split("\n\n")[1].split("(?<=[LR])"))
                .map(Instruction::new)
                .toArray(Instruction[]::new);
    }

    private int directionToValue(Directions direction) {
        return switch (direction) {
            case RIGHT -> 0;
            case DOWN -> 1;
            case LEFT -> 2;
            case UP -> 3;
        };
    }

    private Side parseGrids(String inputString) {
        Tiles[][] inputGrid = parseGrid(inputString);

        Side[] sides = new Side[6];
        int currentSide = 0;

        for (int bigRow = 0; bigRow < 4; bigRow++) {
            for (int bigColumn = 0; bigColumn < 3; bigColumn++) {
                Tiles[][] side = MatrixUtils.subSquare(inputGrid, new Coordinate(CUBE_SIDE_LENGTH * bigRow, CUBE_SIDE_LENGTH * bigColumn), CUBE_SIDE_LENGTH, CUBE_SIDE_LENGTH);

                if (MatrixUtils.coordinateOf(side, Tiles.NULL) != null) {
                    continue;
                }

                sides[currentSide] = new Side(new Coordinate(CUBE_SIDE_LENGTH * bigRow, CUBE_SIDE_LENGTH * bigColumn), side);
                currentSide++;
            }
        }

        // Awfully manual. It was quicker this way.
        sides[0].setUp(sides[5], Directions.RIGHT);
        sides[0].setDown(sides[2], Directions.DOWN);
        sides[0].setLeft(sides[3], Directions.RIGHT);
        sides[0].setRight(sides[1], Directions.RIGHT);

        sides[1].setUp(sides[5], Directions.UP);
        sides[1].setDown(sides[2], Directions.LEFT);
        sides[1].setLeft(sides[0], Directions.LEFT);
        sides[1].setRight(sides[4], Directions.LEFT);

        sides[2].setUp(sides[0], Directions.UP);
        sides[2].setDown(sides[4], Directions.DOWN);
        sides[2].setLeft(sides[3], Directions.DOWN);
        sides[2].setRight(sides[1], Directions.UP);

        sides[3].setUp(sides[2], Directions.RIGHT);
        sides[3].setDown(sides[5], Directions.DOWN);
        sides[3].setLeft(sides[0], Directions.RIGHT);
        sides[3].setRight(sides[4], Directions.RIGHT);

        sides[4].setUp(sides[2], Directions.UP);
        sides[4].setDown(sides[5], Directions.LEFT);
        sides[4].setLeft(sides[3], Directions.LEFT);
        sides[4].setRight(sides[1], Directions.LEFT);

        sides[5].setUp(sides[3], Directions.UP);
        sides[5].setDown(sides[1], Directions.DOWN);
        sides[5].setLeft(sides[0], Directions.DOWN);
        sides[5].setRight(sides[4], Directions.UP);

        return sides[0];
    }

    private Coordinate calculateNewCoordinate(Coordinate currentCoordinate, Directions previousDirection, Directions currentDirection) {
        if (currentDirection == previousDirection) {
            return switch (currentDirection) {
                case UP -> currentCoordinate.setRow(CUBE_SIDE_LENGTH - 1);
                case DOWN -> currentCoordinate.topRow();
                case LEFT -> currentCoordinate.setColumn(CUBE_SIDE_LENGTH - 1);
                case RIGHT -> currentCoordinate.leftmostColumn();
            };
        }

        if (currentDirection == DirectionUtils.opposite(previousDirection)) {
            return switch (currentDirection) {
                case UP, DOWN -> currentCoordinate.setColumn(CUBE_SIDE_LENGTH - currentCoordinate.Column() - 1);
                case LEFT, RIGHT -> currentCoordinate.setRow(CUBE_SIDE_LENGTH - currentCoordinate.Row() - 1);
            };
        }

        return new Coordinate(currentCoordinate.Column(), currentCoordinate.Row());
    }
}
