package advent.of.code.solutions.y2019.day24;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day24 implements Day {
    private final static int ITERATIONS_PART_2 = 200;

    @Override
    public String part1(String inputString) {
        Tiles[][] grid = Arrays.stream(inputString.split("\n"))
                .map(l -> l.chars()
                        .mapToObj(ch -> TilesUtils.charToTile((char) ch))
                        .toArray(Tiles[]::new))
                .toArray(Tiles[][]::new);

        Set<String> seenStates = new HashSet<>();
        seenStates.add(gridToString(grid));

        while (true) {
            grid = execute_part_1(grid);
            String state = gridToString(grid);

            if (seenStates.contains(state)) {
                return String.format("%d", calculateBiodiversityRating(grid));
            }

            seenStates.add(state);
        }
    }

    @Override
    public String part2(String inputString) {
        RecursiveGrid topLevel = createRecursiveGrid(inputString);

        for (int i = 0; i < ITERATIONS_PART_2; i++) {
            topLevel.execute();
        }

        return String.format("%d", topLevel.countAllBugs());
    }

    private String gridToString(Tiles[][] grid) {
        StringBuilder returnValue = new StringBuilder();

        for (Tiles[] tiles : grid) {
            for (int column = 0; column < grid[0].length; column++) {
                returnValue.append(TilesUtils.tileToChar(tiles[column]));
            }
        }

        return returnValue.toString();
    }

    private Tiles[][] execute_part_1(Tiles[][] grid) {
        Tiles[][] newGrid = new Tiles[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {
            System.arraycopy(grid[row], 0, newGrid[row], 0, grid[row].length);
        }

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Coordinate coordinate = new Coordinate(row, column);
                int adjacentBugs = 0;

                for (Coordinate adjacent : coordinate.orthogonallyAdjacent()) {
                    if (MatrixUtils.isCoordInRange(grid, adjacent) && MatrixUtils.getMatrixCoord(grid, adjacent) == Tiles.BUG) {
                        adjacentBugs++;
                    }
                }

                switch (MatrixUtils.getMatrixCoord(grid, coordinate)) {
                    case BUG -> {
                        if (adjacentBugs == 1) {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.BUG);
                        } else {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.EMPTY);
                        }
                    }
                    case EMPTY -> {
                        if (adjacentBugs == 1 || adjacentBugs == 2) {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.BUG);
                        } else {
                            MatrixUtils.setMatrixCoord(newGrid, coordinate, Tiles.EMPTY);
                        }
                    }
                }
            }
        }

        return newGrid;
    }

    private int calculateBiodiversityRating(Tiles[][] grid) {
        int total = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == Tiles.BUG) {
                    int cellId = row * grid.length + column;
                    total += (int) Math.pow(2, cellId);
                }
            }
        }

        return total;
    }

    private RecursiveGrid createRecursiveGrid(String inputString) {
        RecursiveGrid baseLevel = new RecursiveGrid(inputString);
        RecursiveGrid topLevel = baseLevel;
        RecursiveGrid bottomLevel = baseLevel;

        for (int i = 0; i < ITERATIONS_PART_2 / 2 + 1; i++) {
            topLevel = topLevel.addLevelUp();
            bottomLevel = bottomLevel.addLevelDown();
        }

        return topLevel;
    }
}
