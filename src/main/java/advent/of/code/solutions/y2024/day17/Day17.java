package advent.of.code.solutions.y2024.day17;

import advent.of.code.solutions.Day;

@SuppressWarnings("unused")
public class Day17 implements Day {
    @Override
    public String part1(String inputString) {
        Program program = new Program(inputString);
        StringBuilder returnValue = new StringBuilder();

        while (!program.isHalted()) {
            Integer output = program.execute();

            if (output != null) {
                returnValue.append(output);
                returnValue.append(',');
            }
        }

        returnValue.deleteCharAt(returnValue.length() - 1);

        return returnValue.toString();
    }

    /**
     * I don't know how universal or input specific this solution is. It's probably universal enough
     */
    @Override
    public String part2(String inputString) {
        Program program = new Program(inputString);

        long lastA = 0;

        for (Integer target : program.getInstructions().reversed()) {
            for (int i = 0; i < 8; i++) {
                program.clear();
                program.setRegistryA(lastA * 8 + i);
                Integer output = null;

                while (output == null) {
                    output = program.execute();
                }

                if (output.equals(target)) {
                    lastA = lastA * 8 + i;
                    break;
                }
            }
        }

        return String.format("%d", lastA);
    }
}
