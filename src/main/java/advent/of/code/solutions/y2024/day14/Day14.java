package advent.of.code.solutions.y2024.day14;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day14 implements Day {
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    private static final int ITERATIONS_PART_1 = 100;
    private static final int CONTIGUOUS_AREA_THRESHOLD_PART_2 = 50;

    @Override
    public String part1(String inputString) {
        Set<Robot> input = Arrays.stream(inputString.split("\n")).map(Robot::new).collect(Collectors.toSet());
        List<Coordinate> finalCoordinates = input.stream()
                .map(r -> r.move(WIDTH, HEIGHT, ITERATIONS_PART_1))
                .toList();

        int[] quadrants = new int[4];
        Arrays.fill(quadrants, 0);

        int middleColumn = WIDTH / 2;
        int middleRow = HEIGHT / 2;

        for (Coordinate coordinate : finalCoordinates) {
            if (coordinate.Column() < middleColumn && coordinate.Row() < middleRow) {
                quadrants[0]++;
            } else if (coordinate.Column() > middleColumn && coordinate.Row() < middleRow) {
                quadrants[1]++;
            } else if (coordinate.Column() < middleColumn && coordinate.Row() > middleRow) {
                quadrants[2]++;
            } else if (coordinate.Column() > middleColumn && coordinate.Row() > middleRow) {
                quadrants[3]++;
            }
        }

        int total = 1;
        for (int value : quadrants) {
            total *= value;
        }

        return String.format("%d", total);
    }

    /**
     * A comment in
     * <a href="https://www.youtube.com/watch?v=Zyvd-MWo7uE">https://www.youtube.com/watch?v=Zyvd-MWo7uE</a>
     * saved me. I did not like this puzzle.
     */
    @Override
    public String part2(String inputString) {
        Set<Robot> input = Arrays.stream(inputString.split("\n")).map(Robot::new).collect(Collectors.toSet());

        for (int i = 0; true; i++) {
            Set<Coordinate> finalCoordinates = new HashSet<>();
            for (Robot robot : input) {
                finalCoordinates.add(robot.move(WIDTH, HEIGHT, i));
            }

            if (largestContiguous(finalCoordinates) > CONTIGUOUS_AREA_THRESHOLD_PART_2) {
                return String.format("%d", i);
            }
        }
    }

    private int largestContiguous(Set<Coordinate> finalCoordinates) {
        Boolean[][] grid = new Boolean[HEIGHT][WIDTH];
        MatrixUtils.fillMatrix(grid, false);
        Boolean[][] explored = new Boolean[HEIGHT][WIDTH];
        MatrixUtils.fillMatrix(explored, false);

        Set<Integer> contiguousSizes = new HashSet<>();

        for (Coordinate coordinate : finalCoordinates) {
            MatrixUtils.setMatrixCoord(grid, coordinate, true);
        }

        for (int row = 1; row < grid.length - 1; row++) {
            for (int column = 1; column < grid[row].length - 1; column++) {
                if (!grid[row][column] || explored[row][column]) {
                    continue;
                }

                contiguousSizes.add(calculateSize(grid, new Coordinate(row, column), explored));
            }
        }

        return contiguousSizes.stream().mapToInt(l -> l).max().orElse(-1);
    }

    private int calculateSize(Boolean[][] screen, Coordinate coordinate, Boolean[][] explored) {
        Queue<Coordinate> toCheck = new LinkedList<>();
        MatrixUtils.setMatrixCoord(explored, coordinate, true);
        toCheck.add(coordinate);
        int area = 0;

        while (!toCheck.isEmpty()) {
            Coordinate currentCoord = toCheck.poll();
            area++;

            for (Coordinate adjacent : currentCoord.adjacent()) {
                if (!MatrixUtils.isCoordInRange(screen, adjacent)) {
                    continue;
                }

                if (MatrixUtils.getMatrixCoord(screen, adjacent) && !MatrixUtils.getMatrixCoord(explored, adjacent)) {
                    MatrixUtils.setMatrixCoord(explored, adjacent, true);
                    toCheck.add(adjacent);
                }
            }
        }

        return area;
    }
}
