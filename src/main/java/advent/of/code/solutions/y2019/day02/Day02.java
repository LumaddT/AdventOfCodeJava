package advent.of.code.solutions.y2019.day02;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;

@SuppressWarnings("unused")
public class Day02 implements Day {
    private static final int TARGET_OUTPUT = 19_690_720;

    @Override
    public String part1(String inputString) {
        IntcodeProgram program = new IntcodeProgram(inputString);

        program.setMemoryAddress(1, 12);
        program.setMemoryAddress(2, 2);
        program.executeAll();

        return String.format("%d", program.getMemoryAddress(0));
    }

    @Override
    public String part2(String inputString) {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                IntcodeProgram program = new IntcodeProgram(inputString);
                program.setMemoryAddress(1, noun);
                program.setMemoryAddress(2, verb);

                program.executeAll();

                if (program.getMemoryAddress(0) == TARGET_OUTPUT) {
                    return String.format("%d", 100 * noun + verb);
                }
            }
        }

        return "Max iterations reached.";
    }
}
