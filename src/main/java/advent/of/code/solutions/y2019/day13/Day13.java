package advent.of.code.solutions.y2019.day13;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day13 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        int total = 0;

        while (program.executeAll() != IntcodeProgram.ResultCode.HALTED) {
            program.readOutput();
            program.executeAll();
            program.readOutput();
            program.executeAll();
            if (program.readOutput() == 2) {
                total++;
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setMemoryAddress(0, 2);

        long score = playGame(program);

        return String.format("%d", score);
    }

    private long playGame(IntcodeProgram program) {
        int score = 0;
        int paddleColumn = 0;
        int ballColumn = 0;
        int[] outputs = new int[3];

        while (true) {
            IntcodeProgram.ResultCode resultCode = program.executeAll();
            if (resultCode == IntcodeProgram.ResultCode.HALTED) {
                return score;
            } else if (resultCode == IntcodeProgram.ResultCode.WANTS_INPUT) {
                program.setInput(Long.compare(ballColumn, paddleColumn));
                resultCode = program.executeAll();
            }

            if (resultCode != IntcodeProgram.ResultCode.WROTE_OUTPUT) {
                throw new RuntimeException(String.format("Expected WROTE_OUTPUT but got %s", resultCode.toString()));
            }
            outputs[0] = program.readOutputAsInt();

            resultCode = program.executeAll();
            if (resultCode != IntcodeProgram.ResultCode.WROTE_OUTPUT) {
                throw new RuntimeException(String.format("Expected WROTE_OUTPUT but got %s", resultCode.toString()));
            }
            outputs[1] = program.readOutputAsInt();

            resultCode = program.executeAll();
            if (resultCode != IntcodeProgram.ResultCode.WROTE_OUTPUT) {
                throw new RuntimeException(String.format("Expected WROTE_OUTPUT but got %s", resultCode.toString()));
            }
            outputs[2] = program.readOutputAsInt();

            if (outputs[0] == -1 && outputs[1] == 0) {
                score = outputs[2];
            } else if (outputs[2] == 3) {
                paddleColumn = outputs[0];
            } else if (outputs[2] == 4) {
                ballColumn = outputs[0];
            }
        }
    }
}
