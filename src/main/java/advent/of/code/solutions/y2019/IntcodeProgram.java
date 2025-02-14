package advent.of.code.solutions.y2019;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntcodeProgram {
    private static final long DEFAULT_MEMORY_VALUE = 0;

    private final Map<Long, Long> Memory;

    private long ProgramCounter = 0;
    private long RelativeBase = 0;

    private long Input = -1;
    private boolean InputSet = false;

    private long Output = -1;
    private boolean OutputSet = false;

    public IntcodeProgram(String inputString) {
        String[] numbers = inputString.trim().split(",");

        Memory = new HashMap<>(
                IntStream.range(0, numbers.length)
                        .boxed()
                        .collect(Collectors.toMap(i -> (long) i, i -> Long.parseLong(numbers[i])))
        );
    }

    public long getMemoryAddress(long index) {
        return Memory.getOrDefault(index, DEFAULT_MEMORY_VALUE);
    }

    public void setMemoryAddress(long index, long value) {
        Memory.put(index, value);
    }

    public long readOutput() {
        if (!OutputSet) {
            throw new RuntimeException("Attempted to read output while it was not set.");
        }

        OutputSet = false;
        return Output;
    }

    public int readOutputAsInt() {
        if (!OutputSet) {
            throw new RuntimeException("Attempted to read output while it was not set.");
        }

        if (Output > Integer.MAX_VALUE || Output < Integer.MIN_VALUE) {
            throw new RuntimeException("Attempted to read output as int, but output is outside of int range.");
        }

        OutputSet = false;
        return (int) Output;
    }

    public void setInput(long input) {
        if (InputSet) {
            throw new RuntimeException("Attempted to set input while it was already set.");
        }

        Input = input;
        InputSet = true;
    }

    public ResultCode executeAll() {
        while (true) {
            ResultCode resultCode = this.execute();

            if (resultCode != null) {
                return resultCode;
            }
        }
    }

    private ResultCode execute() {
        long fullOpCode = getMemoryAddress(ProgramCounter);
        int opCode = (int) (fullOpCode % 100);
        int modes = (int) (fullOpCode / 100);

        int parametersAmount = getParametersAmount(opCode);
        int[] parameterModes = getParameterModes(modes, parametersAmount);
        long[] parameters = getParameters(parametersAmount);

        long[] values = getValues(opCode, parametersAmount, parameters, parameterModes);

        switch (opCode) {
            case 1 -> Memory.put(values[2], values[0] + values[1]); // Add
            case 2 -> Memory.put(values[2], values[0] * values[1]); // Multiply
            case 3 -> { // Read Input
                if (!InputSet) {
                    return ResultCode.WANTS_INPUT;
                }

                Memory.put(values[0], Input);
                InputSet = false;
            }
            case 4 -> { // Write output
                Output = values[0];
                OutputSet = true;
                ProgramCounter += 2;
                return ResultCode.WROTE_OUTPUT;
            }
            case 5 -> { // Jump not 0
                if (values[0] != 0) {
                    ProgramCounter = values[1] - 3;
                }
            }
            case 6 -> { // Jump eq 0
                if (values[0] == 0) {
                    ProgramCounter = values[1] - 3;
                }
            }
            case 7 -> { // Less than
                if (values[0] < values[1]) {
                    Memory.put(values[2], 1L);
                } else {
                    Memory.put(values[2], 0L);
                }
            }
            case 8 -> { // Equals
                if (values[0] == values[1]) {
                    Memory.put(values[2], 1L);
                } else {
                    Memory.put(values[2], 0L);
                }
            }
            case 9 -> RelativeBase += values[0]; // Add to relative base
            case 99 -> { // Halt
                return ResultCode.HALTED;
            }
            default -> throw new RuntimeException(String.format("Bad opcode %d.", opCode));
        }

        ProgramCounter += parametersAmount + 1;

        return null;
    }

    private int getParametersAmount(int opCode) {
        return switch (opCode) {
            case 3, 4, 9 -> 1; // Read input, Write output, Add to relative base
            case 5, 6 -> 2; // Jump not 0, Jump eq 0
            case 1, 2, 7, 8 -> 3; // Add, Multiply, Less than, Equals
            case 99 -> 0; // Halt
            default -> throw new RuntimeException(String.format("Bad opcode %d.", opCode));
        };
    }

    private int[] getParameterModes(int modes, int parametersAmount) {
        int[] returnValue = new int[parametersAmount];

        for (int i = 0; i < parametersAmount; i++) {
            returnValue[i] = modes % 10;
            modes /= 10;
        }

        return returnValue;
    }

    private long[] getParameters(int parametersAmount) {
        long[] returnValue = new long[parametersAmount];

        for (int i = 0; i < parametersAmount; i++) {
            returnValue[i] = getMemoryAddress(ProgramCounter + i + 1);
        }

        return returnValue;
    }

    private long[] getValues(int opCode, int parametersAmount, long[] parameters, int[] parameterModes) {
        long[] values = new long[parametersAmount];

        if (opCode == 3) { // Read Input
            values[0] = switch (parameterModes[0]) {
                case 0 -> parameters[0];
                case 1 -> throw new RuntimeException("Mode 1 is illegal for opcode 3");
                case 2 -> RelativeBase + parameters[0];
                default -> throw new RuntimeException(String.format("Illegal parameter mode %d;", parameterModes[0]));
            };
        } else {
            if (parametersAmount >= 1) {
                values[0] = switch (parameterModes[0]) {
                    case 0 -> getMemoryAddress(parameters[0]);
                    case 1 -> parameters[0];
                    case 2 -> getMemoryAddress(RelativeBase + parameters[0]);
                    default -> throw new RuntimeException(String.format("Bad parameter mode %d;", parameterModes[0]));
                };
            }
            if (parametersAmount >= 2) {
                values[1] = switch (parameterModes[1]) {
                    case 0 -> getMemoryAddress(parameters[1]);
                    case 1 -> parameters[1];
                    case 2 -> getMemoryAddress(RelativeBase + parameters[1]);
                    default -> throw new RuntimeException(String.format("Bad parameter mode %d;", parameterModes[1]));
                };
            }
            if (parametersAmount >= 3) { // Add, Multiply, Less than, Equals
                values[2] = switch (parameterModes[2]) {
                    case 0 -> parameters[2];
                    case 1 -> throw new RuntimeException(String.format("Mode 1 bad for opcode %d", opCode));
                    case 2 -> RelativeBase + parameters[2];
                    default -> throw new RuntimeException(String.format("Bad parameter mode %d;", parameterModes[2]));
                };
            }
            if (parametersAmount > 3) {
                throw new RuntimeException(String.format("Bad parameters amount %d", parametersAmount));
            }
        }

        return values;
    }

    public enum ResultCode {
        WANTS_INPUT, WROTE_OUTPUT, HALTED
    }
}