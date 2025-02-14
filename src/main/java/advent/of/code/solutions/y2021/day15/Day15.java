package advent.of.code.solutions.y2021.day15;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unused")
public class Day15 implements Day {
    private static final int EXPANSION_FACTOR = 5;

    @Override
    public String part1(String inputString) {
        Integer[][] input = ParsingUtils.toIntegerGrid(inputString);

        return String.format("%d", riskScore(input));
    }

    @Override
    public String part2(String inputString) {
        Integer[][] input = ParsingUtils.toIntegerGrid(inputString);

        input = expandGrid(input);

        return String.format("%d", riskScore(input));
    }

    private int riskScore(Integer[][] input) {
        Integer[][] risk = new Integer[input.length][input[0].length];
        MatrixUtils.fillMatrix(risk, Integer.MAX_VALUE);
        risk[0][0] = 0;

        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(Coordinate.ORIGIN);

        while (!toCheck.isEmpty()) {
            Coordinate checked = toCheck.poll();

            for (Coordinate adjacent : checked.orthogonallyAdjacent()) {
                if (!MatrixUtils.isCoordInRange(input, adjacent)) {
                    continue;
                }

                int newValue = MatrixUtils.getMatrixCoord(risk, checked) + MatrixUtils.getMatrixCoord(input, adjacent);

                if (newValue < MatrixUtils.getMatrixCoord(risk, adjacent)) {
                    MatrixUtils.setMatrixCoord(risk, adjacent, newValue);
                    toCheck.add(adjacent);
                }
            }
        }

        return risk[risk.length - 1][risk[0].length - 1];
    }

    private Integer[][] expandGrid(Integer[][] input) {
        Integer[][] returnValue = new Integer[input.length * EXPANSION_FACTOR][input[0].length * EXPANSION_FACTOR];

        for (int row = 0; row < returnValue.length; row++) {
            for (int column = 0; column < returnValue[0].length; column++) {
                int increment = row / input.length + column / input.length;

                int newValue = input[row % input.length][column % input[0].length] + increment;
                while (newValue >= 10) {
                    newValue -= 9;
                }

                returnValue[row][column] = newValue;
            }
        }

        return returnValue;
    }
}
