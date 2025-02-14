package advent.of.code.solutions.y2024.day18;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("unused")
public class Day18 implements Day {
    private static final int HEIGHT = 71;
    private static final int WIDTH = 71;
    private static final Coordinate INITIAL_COORDINATE = Coordinate.ORIGIN;
    private static final Coordinate TARGET_COORDINATE = new Coordinate(HEIGHT - 1, WIDTH - 1);

    private static final int PART_1_BYTES = 1024;

    @Override
    public String part1(String inputString) {
        List<Coordinate> input = parseInputString(inputString)
                .subList(0, PART_1_BYTES);

        Boolean[][] grid = new Boolean[HEIGHT][WIDTH];
        MatrixUtils.fillMatrix(grid, false);
        for (Coordinate coordinate : input) {
            MatrixUtils.setMatrixCoord(grid, coordinate, true);
        }

        return String.format("%d", findDistanceToEnd(grid));
    }

    @Override
    public String part2(String inputString) {
        List<Coordinate> input = parseInputString(inputString);

        Boolean[][] grid = new Boolean[HEIGHT][WIDTH];
        MatrixUtils.fillMatrix(grid, false);
        for (Coordinate coordinate : input.subList(0, PART_1_BYTES)) {
            MatrixUtils.setMatrixCoord(grid, coordinate, true);
        }

        for (Coordinate coordinate : input.subList(PART_1_BYTES, input.size())) {
            MatrixUtils.setMatrixCoord(grid, coordinate, true);
            int distance = findDistanceToEnd(grid);

            if (distance == Integer.MAX_VALUE) {
                return String.format("%d,%d", coordinate.Column(), coordinate.Row());
            }
        }

        throw new RuntimeException("Bad solution.");
    }

    private List<Coordinate> parseInputString(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(l -> l.split(","))
                .map(l -> new Coordinate(Integer.parseInt(l[1]), Integer.parseInt(l[0])))
                .toList();
    }

    private int findDistanceToEnd(Boolean[][] grid) {
        Integer[][] distances = new Integer[HEIGHT][WIDTH];
        MatrixUtils.fillMatrix(distances, Integer.MAX_VALUE);

        MatrixUtils.setMatrixCoord(distances, INITIAL_COORDINATE, 0);
        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(INITIAL_COORDINATE);

        while (!toCheck.isEmpty()) {
            Coordinate currentCoordinate = toCheck.poll();

            for (Coordinate nextCoordinate : currentCoordinate.orthogonallyAdjacent()) {
                if (!MatrixUtils.isCoordInRange(grid, nextCoordinate) || MatrixUtils.getMatrixCoord(grid, nextCoordinate)) {
                    continue;
                }

                int currentValue = MatrixUtils.getMatrixCoord(distances, nextCoordinate);
                int newValue = MatrixUtils.getMatrixCoord(distances, currentCoordinate) + 1;

                if (newValue < currentValue) {
                    MatrixUtils.setMatrixCoord(distances, nextCoordinate, newValue);
                    toCheck.add(nextCoordinate);
                }
            }
        }

        return MatrixUtils.getMatrixCoord(distances, TARGET_COORDINATE);
    }
}
