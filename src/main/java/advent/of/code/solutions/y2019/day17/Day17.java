package advent.of.code.solutions.y2019.day17;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Day17 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        StringBuilder cameraView = new StringBuilder();

        while (program.executeAll() != IntcodeProgram.ResultCode.HALTED) {
            cameraView.append((char) Math.toIntExact(program.readOutput()));
        }

        char[][] cameraViewGrid = Arrays.stream(cameraView.toString().split("\n")).map(String::toCharArray).toArray(char[][]::new);
        int result = 0;

        for (int row = 1; row < cameraViewGrid.length - 1; row++) {
            for (int column = 1; column < cameraViewGrid[row].length - 1; column++) {
                if (isIntersection(row, column, cameraViewGrid)) {
                    result += row * column;
                }
            }
        }

        return String.format("%d", result);
    }

    /**
     * It would be interesting to find programmatically the solution for an arbitrary input.
     * I thought it was easier to find my solution manually and hardcode it.
     */
    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setMemoryAddress(0, 2);

        char[] solution = ("""
                A,B,B,C,C,A,B,B,C,A
                R,4,R,12,R,10,L,12
                L,12,R,4,R,12
                L,12,L,8,R,10
                n
                """)
                .toCharArray();

        for (char ch : solution) {
            IntcodeProgram.ResultCode resultCode;
            do {
                resultCode = program.executeAll();
            } while (resultCode != IntcodeProgram.ResultCode.WANTS_INPUT);

            program.setInput(ch);
        }

        IntcodeProgram.ResultCode resultCode;
        do {
            resultCode = program.executeAll();
        } while (resultCode != IntcodeProgram.ResultCode.HALTED);

        return (String.format("%d", program.readOutput()));
    }

    private boolean isIntersection(int row, int column, char[][] cameraViewGrid) {
        return cameraViewGrid[row][column] != '.'
                && cameraViewGrid[row][column - 1] != '.'
                && cameraViewGrid[row][column + 1] != '.'
                && cameraViewGrid[row - 1][column] != '.'
                && cameraViewGrid[row + 1][column] != '.';
    }
}
