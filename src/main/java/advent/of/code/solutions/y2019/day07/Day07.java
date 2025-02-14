package advent.of.code.solutions.y2019.day07;

import advent.of.code.solutions.Day;
import advent.of.code.solutions.y2019.IntcodeProgram;
import advent.of.code.utils.CombinatoricsUtils;

import java.util.List;
import java.util.stream.IntStream;


@SuppressWarnings("unused")
public class Day07 implements Day {
    private static final List<Integer> PHASE_SETTINGS_PART_1 = IntStream.range(0, 5).boxed().toList();
    private static final List<Integer> PHASE_SETTINGS_PART_2 = IntStream.range(5, 10).boxed().toList();

    @Override
    public String part1(String inputString) {
        List<List<Integer>> permutations = CombinatoricsUtils.generatePermutations(PHASE_SETTINGS_PART_1);

        return String.format("%d", permutations.stream()
                .mapToInt(p -> calculateOutputPart1(inputString, p))
                .max()
                .orElseThrow());
    }

    @Override
    public String part2(String inputString) {
        List<List<Integer>> permutations = CombinatoricsUtils.generatePermutations(PHASE_SETTINGS_PART_2);

        return String.format("%d", permutations.stream()
                .mapToInt(p -> calculateOutputPart2(inputString, p))
                .max()
                .orElseThrow());
    }

    private int calculateOutputPart1(String inputString, List<Integer> phaseSettings) {
        IntcodeProgram[] programs = new IntcodeProgram[phaseSettings.size()];
        for (int i = 0; i < phaseSettings.size(); i++) {
            programs[i] = new IntcodeProgram(inputString);
            programs[i].setInput(phaseSettings.get(i));
            programs[i].executeAll();
        }

        long output = 0;

        for (IntcodeProgram program : programs) {
            program.setInput(output);
            program.executeAll();
            output = program.readOutput();
        }

        return (int) output;
    }

    private int calculateOutputPart2(String inputString, List<Integer> phaseSettings) {
        IntcodeProgram[] programs = new IntcodeProgram[phaseSettings.size()];
        for (int i = 0; i < phaseSettings.size(); i++) {
            programs[i] = new IntcodeProgram(inputString);
            programs[i].setInput(phaseSettings.get(i));
            programs[i].executeAll();
        }

        long output = 0;
        long lastThrusterOutput = output;

        while (true) {
            for (IntcodeProgram program : programs) {
                program.setInput(output);

                if (program.executeAll() == IntcodeProgram.ResultCode.HALTED) {
                    return (int) lastThrusterOutput;
                }

                output = program.readOutput();
            }

            lastThrusterOutput = output;
        }
    }
}
