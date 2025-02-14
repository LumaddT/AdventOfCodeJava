package advent.of.code.solutions.y2024.day09;

import advent.of.code.solutions.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day09 implements Day {
    @Override
    public String part1(String inputString) {
        List<Integer> input = inputString.trim().chars().map(c -> c - '0').boxed().toList();
        int[] hardDrive = inputToDrive(input);

        int leftIndex = 0;
        int rightIndex = hardDrive.length - 1;

        while (rightIndex > leftIndex) {
            while (hardDrive[leftIndex] != -1) {
                leftIndex++;
            }

            while (hardDrive[rightIndex] == -1) {
                rightIndex--;
            }

            if (rightIndex <= leftIndex) {
                break;
            }

            hardDrive[leftIndex] = hardDrive[rightIndex];
            hardDrive[rightIndex] = -1;
        }

        return String.format("%d", checksum(hardDrive));
    }

    /**
     * This solution takes a healthy 20 seconds on my machine. It can probably be better but it works.
     * Maybe a lot of time is taken sorting the keyset of emptySpaces at each iteration. It could be made
     * with a list and binary insertion.
     */
    @Override
    public String part2(String inputString) {
        List<Integer> input = inputString.trim().chars().map(c -> c - '0').boxed().toList();
        int[] hardDrive = inputToDrive(input);

        Map<Integer, Integer> emptySpaces = findEmptySpaces(hardDrive);

        int rightIndex = hardDrive.length - 1;

        while (true) {
            while (hardDrive[rightIndex] == -1) {
                rightIndex--;
            }

            int fileIndex = hardDrive[rightIndex];

            if (fileIndex == 0) {
                break;
            }

            int fileLength = 0;

            while (hardDrive[rightIndex] == fileIndex) {
                fileLength++;
                rightIndex--;
            }

            for (int index : emptySpaces.keySet().stream().sorted().toList()) {
                if (index > rightIndex) {
                    break;
                }

                if (emptySpaces.get(index) >= fileLength) {
                    for (int offsetIndex = index; offsetIndex < index + fileLength; offsetIndex++) {
                        hardDrive[offsetIndex] = fileIndex;
                    }

                    for (int offsetIndex = rightIndex + 1; offsetIndex < rightIndex + fileLength + 1; offsetIndex++) {
                        hardDrive[offsetIndex] = -1;
                    }

                    emptySpaces.put(index + fileLength, emptySpaces.get(index) - fileLength);
                    emptySpaces.remove(index);
                    break;
                }
            }
        }

        return String.format("%d", checksum(hardDrive));
    }


    private int[] inputToDrive(List<Integer> input) {
        int totalCapacity = input.stream().mapToInt(n -> n).sum();
        int[] hardDrive = new int[totalCapacity];
        int hardDriveIndex = 0;

        for (int region = 0; region < input.size() / 2; region++) {
            for (int i = 0; i < input.get(region * 2); i++) {
                hardDrive[hardDriveIndex] = region;
                hardDriveIndex++;
            }

            for (int i = 0; i < input.get(region * 2 + 1); i++) {
                hardDrive[hardDriveIndex] = -1;
                hardDriveIndex++;
            }
        }

        for (int i = 0; i < input.getLast(); i++) {
            hardDrive[hardDriveIndex] = input.size() / 2;
            hardDriveIndex++;
        }

        return hardDrive;
    }

    private long checksum(int[] hardDrive) {
        long checksum = 0;

        for (int i = 0; i < hardDrive.length; i++) {
            if (hardDrive[i] != -1) {
                checksum += (long) hardDrive[i] * i;
            }
        }

        return checksum;
    }

    private Map<Integer, Integer> findEmptySpaces(int[] hardDrive) {
        Map<Integer, Integer> returnValue = new HashMap<>(); // <FirstIndex, Length>

        int i = 0;
        do {
            while (hardDrive[i] != -1 && hardDrive[i] != hardDrive[hardDrive.length - 1]) {
                i++;
            }

            if (hardDrive[i] == hardDrive[hardDrive.length - 1]) {
                return returnValue;
            }

            int firstIndex = i;

            int length = 0;
            while (hardDrive[i] == -1) {
                i++;
                length++;
            }

            returnValue.put(firstIndex, length);

        } while (hardDrive[i] != hardDrive[hardDrive.length - 1]);

        return returnValue;
    }
}
