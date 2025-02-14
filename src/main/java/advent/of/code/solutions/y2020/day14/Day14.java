package advent.of.code.solutions.y2020.day14;

import advent.of.code.solutions.Day;
import advent.of.code.utils.SetUtils;

import java.util.*;

@SuppressWarnings("unused")
public class Day14 implements Day {
    private static final int MASK_LENGTH = 36;

    @Override
    public String part1(String inputString) {
        String[] input = inputString.split("\n");
        char[] mask = null;
        Map<Long, Long> memory = new HashMap<>();

        for (String line : input) {
            String[] lineSplit = line.split(" ");

            if (lineSplit[0].equals("mask")) {
                mask = lineSplit[2].toCharArray();
            } else {
                long address = Long.parseLong(lineSplit[0].substring(4, lineSplit[0].length() - 1));
                long value = Long.parseLong(lineSplit[2]);
                value = applyMaskPart1(value, mask);

                memory.put(address, value);
            }
        }

        return String.format("%d", memory.values().stream().mapToLong(v -> v).sum());
    }

    @Override
    public String part2(String inputString) {

        String[] input = inputString.split("\n");
        char[] mask = null;
        Map<Long, Long> memory = new HashMap<>();

        for (String line : input) {
            String[] lineSplit = line.split(" ");

            if (lineSplit[0].equals("mask")) {
                mask = lineSplit[2].toCharArray();
            } else {
                long baseAddress = Integer.parseInt(lineSplit[0].substring(4, lineSplit[0].length() - 1));
                Set<Long> addresses = applyMaskPart2(baseAddress, mask);
                long value = Long.parseLong(lineSplit[2]);

                for (long address : addresses) {
                    memory.put(address, value);
                }
            }
        }

        return String.format("%d", memory.values().stream().mapToLong(v -> v).sum());
    }

    private long applyMaskPart1(long value, char[] mask) {
        int[] binaryRepresentation = longToBinary(value);

        int[] resultBinary = new int[MASK_LENGTH];

        for (int i = 0; i < MASK_LENGTH; i++) {
            resultBinary[i] = switch (mask[i]) {
                case '0' -> 0;
                case '1' -> 1;
                case 'X' -> binaryRepresentation[i];
                default -> throw new IllegalStateException("Unexpected value: " + mask[i]);
            };
        }

        return binaryToLong(resultBinary);
    }

    private Set<Long> applyMaskPart2(long value, char[] mask) {
        int[] binaryRepresentation = longToBinary(value);

        int[] resultBinary = new int[MASK_LENGTH];
        List<Integer> floatingIndexes = new ArrayList<>();

        for (int i = 0; i < MASK_LENGTH; i++) {
            resultBinary[i] = switch (mask[i]) {
                case '0' -> binaryRepresentation[i];
                case '1' -> 1;
                case 'X' -> {
                    floatingIndexes.add(i);
                    yield 0;
                }
                default -> throw new IllegalStateException("Unexpected value: " + mask[i]);
            };
        }

        return calculateAllIndexes(resultBinary, floatingIndexes);
    }

    private int[] longToBinary(long value) {
        int[] binaryRepresentation = new int[MASK_LENGTH];

        for (int i = 0; i < MASK_LENGTH; i++) {
            binaryRepresentation[MASK_LENGTH - i - 1] = (int) (value % 2);
            value /= 2;
        }

        return binaryRepresentation;
    }

    private long binaryToLong(int[] resultBinary) {
        long value = 0;

        for (int i = 0; i < MASK_LENGTH; i++) {
            value += resultBinary[MASK_LENGTH - i - 1] * (long) Math.pow(2, i);
        }

        return value;
    }

    private Set<Long> calculateAllIndexes(int[] binaryArray, List<Integer> floatingIndexes) {
        Set<Long> returnValue = new HashSet<>();

        Set<Set<Integer>> powerSet = SetUtils.powerSet(floatingIndexes);

        for (Set<Integer> indexesToTurn : powerSet) {
            int[] result = Arrays.copyOf(binaryArray, binaryArray.length);

            for (int index : indexesToTurn) {
                result[index] = 1;
            }

            returnValue.add(binaryToLong(result));
        }

        return Collections.unmodifiableSet(returnValue);
    }
}
