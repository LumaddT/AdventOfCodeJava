package advent.of.code.solutions.y2019.day05;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day05 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setInput(1);

        while (true) {
            program.executeAll();
            long output = program.readOutput();

            if (output != 0) {
                return String.format("%d", output);
            }
        }
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setInput(5);

        program.executeAll();

        return String.format("%d", program.readOutput());
    }
}
