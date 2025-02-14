package advent.of.code.solutions.y2019.day19;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day19 implements Day {
    private static final int ROWS_PART_1 = 50;
    private static final int COLUMNS_PART_1 = 50;

    private static final int SANTA_SHIP_ROWS = 100;
    private static final int SANTA_SHIP_COLUMNS = 100;

    @Override
    public String part1(String inputString) {
        int total = 0;

        for (int row = 0; row < ROWS_PART_1; row++) {
            for (int column = 0; column < COLUMNS_PART_1; column++) {
                if (isTractorBeam(row, column, inputString)) {
                    total++;
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        int row = 25;
        int rightmostBeamColumn;
        boolean hasFoundBeam = false;

        for (int i = 0; true; i++) {
            boolean isBeam = isTractorBeam(row, i, inputString);

            if (isBeam) {
                hasFoundBeam = true;
            }

            if (!isBeam && hasFoundBeam) {
                rightmostBeamColumn = i - 1;
                break;
            }
        }

        while (true) {
            row++;
            while (isTractorBeam(row, rightmostBeamColumn, inputString)) {
                rightmostBeamColumn++;
            }

            rightmostBeamColumn--;

            if (isTractorBeam(row + SANTA_SHIP_ROWS - 1, rightmostBeamColumn - SANTA_SHIP_COLUMNS + 1, inputString)) {
                return String.format("%d", (rightmostBeamColumn - SANTA_SHIP_COLUMNS + 1) * 10_000 + row);
            }
        }
    }

    private boolean isTractorBeam(int row, int column, String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        program.setInput(column);
        program.executeAll();
        program.setInput(row);
        program.executeAll();
        return program.readOutput() == 1;
    }
}
