package advent.of.code.solutions.y2019.day21;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day21 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        // jump if any of the 3 closest tiles is empty
        write(program, "NOT A T");
        write(program, "OR T J");
        write(program, "NOT B T");
        write(program, "OR T J");
        write(program, "NOT C T");
        write(program, "OR T J");

        // but only if the 4th is solid
        write(program, "AND D J");

        write(program, "WALK");

        long output;
        do {
            program.executeAll();
            output = program.readOutput();
        } while (output < 256);

        return String.format("%d", output);
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        // jump if any of the 3 closest tiles is empty
        write(program, "NOT A T");
        write(program, "OR T J");
        write(program, "NOT B T");
        write(program, "OR T J");
        write(program, "NOT C T");
        write(program, "OR T J");

        // but only if the 4th is solid, and if either 5th or 8th is solid
        write(program, "AND D J");
        write(program, "NOT H T");
        write(program, "NOT T T");
        write(program, "OR E T");
        write(program, "AND T J");

        write(program, "RUN");

        long output;
        do {
            program.executeAll();
            output = program.readOutput();
        } while (output < 256);

        return String.format("%d", output);
    }

    private void write(IntcodeProgram program, String input) {
        while (program.executeAll() == IntcodeProgram.ResultCode.WROTE_OUTPUT) {
            program.readOutput();
        }
        for (char c : input.toCharArray()) {
            program.setInput(c);
            while (program.executeAll() == IntcodeProgram.ResultCode.WROTE_OUTPUT) {
                program.readOutput();
            }
        }

        while (program.executeAll() == IntcodeProgram.ResultCode.WROTE_OUTPUT) {
            program.readOutput();
        }
        program.setInput('\n');
    }
}
