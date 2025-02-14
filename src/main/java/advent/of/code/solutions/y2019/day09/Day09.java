package advent.of.code.solutions.y2019.day09;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day09 implements Day {
    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setInput(1);

        program.executeAll();

        return String.format("%d", program.readOutput());
    }

    @Override
    public String part2(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);
        program.setInput(2);

        program.executeAll();

        return String.format("%d", program.readOutput());

    }
}
