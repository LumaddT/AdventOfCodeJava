package advent.of.code.solutions.y2024.day12;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day12 implements Day {
    @Override
    public String part1(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);
        Character[][] framedInput = MatrixUtils.addFrame(input, '#');
        Set<Coordinate> explored = new HashSet<>();

        long total = 0;

        for (int row = 1; row < framedInput.length - 1; row++) {
            for (int column = 1; column < framedInput[row].length - 1; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                if (explored.contains(coordinate)) {
                    continue;
                }

                total += calculateCostPart1(framedInput, coordinate, explored);
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Character[][] input = ParsingUtils.toGridBoxed(inputString);
        Character[][] framedInput = MatrixUtils.addFrame(input, '#');
        Set<Coordinate> explored = new HashSet<>();

        long total = 0;

        for (int row = 1; row < framedInput.length - 1; row++) {
            for (int column = 1; column < framedInput[row].length - 1; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                if (explored.contains(coordinate)) {
                    continue;
                }

                total += calculateCostPart2(framedInput, coordinate, explored);
            }
        }

        return String.format("%d", total);
    }

    private int calculateCostPart1(Character[][] field, Coordinate coordinate, Set<Coordinate> explored) {
        char letter = MatrixUtils.getMatrixCoord(field, coordinate);
        Queue<Coordinate> toCheck = new LinkedList<>();
        explored.add(coordinate);
        toCheck.add(coordinate);
        int perimeter = 0;
        int area = 0;

        while (!toCheck.isEmpty()) {
            Coordinate currentCoord = toCheck.poll();
            area++;
            int localPerimeter = 0;

            for (Coordinate adjacent : currentCoord.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(field, adjacent) != letter) {
                    localPerimeter++;
                } else if (!explored.contains(adjacent)) {
                    explored.add(adjacent);
                    toCheck.add(adjacent);
                }
            }

            perimeter += localPerimeter;
        }

        return perimeter * area;
    }

    private long calculateCostPart2(Character[][] field, Coordinate coordinate, Set<Coordinate> explored) {
        char letter = MatrixUtils.getMatrixCoord(field, coordinate);
        Queue<Coordinate> toCheck = new LinkedList<>();
        explored.add(coordinate);
        toCheck.add(coordinate);
        Boolean[][] thisPlot = new Boolean[field.length][field[0].length];
        MatrixUtils.fillMatrix(thisPlot, false);
        long area = 0;

        while (!toCheck.isEmpty()) {
            Coordinate currentCoord = toCheck.poll();
            area++;
            MatrixUtils.setMatrixCoord(thisPlot, currentCoord, true);

            for (Coordinate adjacent : currentCoord.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(field, adjacent) == letter && !explored.contains(adjacent)) {
                    explored.add(adjacent);
                    toCheck.add(adjacent);
                }
            }
        }

        int sides = calculateSides(thisPlot);

        return sides * area;
    }

    private int calculateSides(Boolean[][] thisPlot) {
        int sides = 0;
        Coordinate currentCoordinate = new Coordinate(0, 1);

        while (MatrixUtils.isCoordInRange(thisPlot, currentCoordinate)) {
            while (MatrixUtils.isCoordInRange(thisPlot, currentCoordinate)) {
                if (MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.left()) && !MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                    sides++;
                    while (MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.left()) && !MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                        currentCoordinate = currentCoordinate.down();
                    }
                } else if (!MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.left()) && MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                    sides++;
                    while (!MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.left()) && MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                        currentCoordinate = currentCoordinate.down();
                    }
                } else {
                    currentCoordinate = currentCoordinate.down();
                }
            }

            currentCoordinate = currentCoordinate.topRow().right();
        }

        currentCoordinate = new Coordinate(1, 0);

        while (MatrixUtils.isCoordInRange(thisPlot, currentCoordinate)) {
            while (MatrixUtils.isCoordInRange(thisPlot, currentCoordinate)) {
                if (MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.up()) && !MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                    sides++;
                    while (MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.up()) && !MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                        currentCoordinate = currentCoordinate.right();
                    }
                } else if (!MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.up()) && MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                    sides++;
                    while (!MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate.up()) && MatrixUtils.getMatrixCoord(thisPlot, currentCoordinate)) {
                        currentCoordinate = currentCoordinate.right();
                    }
                } else {
                    currentCoordinate = currentCoordinate.right();
                }
            }

            currentCoordinate = currentCoordinate.leftmostColumn().down();
        }

        return sides;
    }
}
