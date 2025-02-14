package advent.of.code.solutions.y2020.day08;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day08 implements Day {
    @Override
    public String part1(String inputString) {
        Tuple<Instructions[], int[]> input = parseInputString(inputString);
        Instructions[] instructions = input.First();
        int[] amounts = input.Second();

        Integer result = executeProgram(instructions, amounts).Second();

        return String.format("%d", result);
    }

    @Override
    public String part2(String inputString) {
        Tuple<Instructions[], int[]> input = parseInputString(inputString);
        Instructions[] instructions = input.First();
        int[] amounts = input.Second();

        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i] == Instructions.ACC) {
                continue;
            }

            flipJmpAndNop(instructions, i);

            Tuple<Boolean, Integer> result = executeProgram(instructions, amounts);
            if (result.First()) {
                return String.format("%d", result.Second());
            }

            flipJmpAndNop(instructions, i);
        }

        throw new RuntimeException("Bad solution.");
    }

    private Tuple<Instructions[], int[]> parseInputString(String inputString) {
        String[] inputSplit = inputString.split("\n");

        Instructions[] instructions = new Instructions[inputSplit.length];
        int[] amounts = new int[inputSplit.length];

        for (int i = 0; i < inputSplit.length; i++) {
            String[] lineSplit = inputSplit[i].split(" ");
            amounts[i] = Integer.parseInt(lineSplit[1]);

            instructions[i] = switch (lineSplit[0]) {
                case "acc" -> Instructions.ACC;
                case "jmp" -> Instructions.JMP;
                case "nop" -> Instructions.NOP;
                default -> throw new IllegalStateException("Unexpected value: " + lineSplit[0]);
            };
        }

        return new Tuple<>(instructions, amounts);
    }

    /**
     * Returns the value of the accumulator as the second member of a Tuple. The first value is true
     * if the program halted, and false otherwise.
     */
    private Tuple<Boolean, Integer> executeProgram(Instructions[] instructions, int[] amounts) {
        int accumulator = 0;
        int programCounter = 0;

        Set<Integer> seenInstructions = new HashSet<>();

        while (programCounter < instructions.length) {
            if (seenInstructions.contains(programCounter)) {
                return new Tuple<>(false, accumulator);
            }

            seenInstructions.add(programCounter);

            switch (instructions[programCounter]) {
                case ACC -> {
                    accumulator += amounts[programCounter];
                    programCounter++;
                }
                case JMP -> programCounter += amounts[programCounter];
                case NOP -> programCounter++;
            }
        }

        return new Tuple<>(true, accumulator);
    }

    private void flipJmpAndNop(Instructions[] instructions, int i) {
        instructions[i] = switch (instructions[i]) {
            case ACC -> throw new RuntimeException("Bad flipping.");
            case JMP -> Instructions.NOP;
            case NOP -> Instructions.JMP;
        };
    }
}
