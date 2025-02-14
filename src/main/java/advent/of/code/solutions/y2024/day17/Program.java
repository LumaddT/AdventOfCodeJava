package advent.of.code.solutions.y2024.day17;

import advent.of.code.utils.RegexUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

class Program {
    private final static List<Integer> COMBO_OPCODES = List.of(0, 2, 5, 6, 7);

    @Setter
    private long RegistryA;
    private long RegistryB;
    private long RegistryC;

    @Getter
    private final List<Integer> Instructions;

    private int InstructionPointer = 0;

    public Program(String inputString) {
        String regex = "^Register A: (\\d+)\nRegister B: (\\d+)\nRegister C: (\\d+)\n\nProgram: ([\\d,]+)$";
        Matcher matcher = RegexUtils.match(regex, inputString.trim(), 4);

        RegistryA = Long.parseLong(matcher.group(1));
        RegistryB = Long.parseLong(matcher.group(2));
        RegistryC = Long.parseLong(matcher.group(3));
        Instructions = Arrays.stream(matcher.group(4).split(",")).map(Integer::parseInt).toList();
    }

    public Integer execute() {
        int opCode = Instructions.get(InstructionPointer);
        int operand = Instructions.get(InstructionPointer + 1);
        long value = getValue(opCode, operand);
        Integer output = null;

        switch (opCode) {
            case 0 -> RegistryA = (long) (RegistryA / Math.pow(2, value)); // adv
            case 1 -> RegistryB ^= value; // bxl
            case 2 -> RegistryB = value % 8; // bst
            case 3 -> InstructionPointer = (RegistryA != 0) ? (int) value - 2 : InstructionPointer; // jnz
            case 4 -> RegistryB ^= RegistryC; // bxc
            case 5 -> output = (int) (value % 8); // out
            case 6 -> RegistryB = (long) (RegistryA / Math.pow(2, value)); // bdv
            case 7 -> RegistryC = (long) (RegistryA / Math.pow(2, value)); // cdv
            default -> throw new RuntimeException(String.format("Illegal opCode %d", opCode));
        }

        InstructionPointer += 2;

        return output;
    }

    private long getValue(int opCode, int operand) {
        if (COMBO_OPCODES.contains(opCode)) {
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> RegistryA;
                case 5 -> RegistryB;
                case 6 -> RegistryC;
                case 7 -> throw new RuntimeException("Reserved operand 7.");
                default -> throw new RuntimeException(String.format("Invalid operand %d", operand));
            };
        }

        return operand;
    }

    public boolean isHalted() {
        return InstructionPointer >= Instructions.size();
    }

    public void clear() {
        RegistryA = 0;
        RegistryB = 0;
        RegistryC = 0;
        InstructionPointer = 0;
    }
}
