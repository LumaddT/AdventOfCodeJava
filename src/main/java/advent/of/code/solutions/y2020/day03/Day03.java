package advent.of.code.solutions.y2020.day03;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.List;

@SuppressWarnings("unused")
public class Day03 implements Day {
    private static final List<Coordinate> SLOPES = List.of(
            new Coordinate(1, 1),
            new Coordinate(1, 3),
            new Coordinate(1, 5),
            new Coordinate(1, 7),
            new Coordinate(2, 1)
    );

    private static final Coordinate SLOPE_PART_1 = SLOPES.get(1);

    @Override
    public String part1(String inputString) {
        Character[][] grid = ParsingUtils.toGridBoxed(inputString);

        Coordinate currentCoordinate = Coordinate.ORIGIN;

        int total = 0;

        while (MatrixUtils.isCoordInRange(grid, currentCoordinate)) {
            if (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == '#') {
                total++;
            }

            currentCoordinate = currentCoordinate.add(SLOPE_PART_1).columnModulo(grid[0].length);
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Character[][] grid = ParsingUtils.toGridBoxed(inputString);

        int total = 1;

        for (Coordinate slope : SLOPES) {
            Coordinate currentCoordinate = Coordinate.ORIGIN;
            int slopeTotal = 0;

            while (MatrixUtils.isCoordInRange(grid, currentCoordinate)) {
                if (MatrixUtils.getMatrixCoord(grid, currentCoordinate) == '#') {
                    slopeTotal++;
                }

                currentCoordinate = currentCoordinate.add(slope).columnModulo(grid[0].length);
            }

            total *= slopeTotal;
        }

        return String.format("%d", total);
    }
}
