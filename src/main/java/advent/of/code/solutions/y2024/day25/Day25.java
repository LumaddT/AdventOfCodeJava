package advent.of.code.solutions.y2024.day25;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.ntuples.Tuple;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class Day25 implements Day {
    private final static int LOCK_HEIGHT = 5;

    @Override
    public String part1(String inputString) {
        Tuple<Set<int[]>, Set<int[]>> locksKeys = parseInputString(inputString);

        Set<int[]> locks = locksKeys.First();
        Set<int[]> keys = locksKeys.Second();

        int total = 0;

        for (int[] lock : locks) {
            for (int[] key : keys) {
                if (lockAndKeyFit(lock, key)) {
                    total++;
                }
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        return "No part 2 on Christmas.";
    }

    private Tuple<Set<int[]>, Set<int[]>> parseInputString(String inputString) {
        Set<int[]> locks = new HashSet<>();
        Set<int[]> keys = new HashSet<>();

        for (String device : inputString.split("\n\n")) {
            char[][] grid = ParsingUtils.toGrid(device);

            if (isLock(grid)) {
                locks.add(parseLock(grid));
            } else {
                keys.add(parseKey(grid));
            }
        }

        return new Tuple<>(locks, keys);
    }

    private boolean isLock(char[][] grid) {
        return grid[0][0] == '#';
    }

    private int[] parseLock(char[][] grid) {
        int[] lock = new int[grid[0].length];

        for (int column = 0; column < grid[0].length; column++) {
            int height = 0;
            for (int row = 1; row < grid.length; row++) {
                if (grid[row][column] == '#') {
                    height++;
                }
            }

            lock[column] = height;
        }

        return lock;
    }

    private int[] parseKey(char[][] grid) {
        int[] key = new int[grid[0].length];

        for (int column = 0; column < grid[0].length; column++) {
            int height = 0;
            for (int row = grid.length - 2; row > 0; row--) {
                if (grid[row][column] == '#') {
                    height++;
                }
            }

            key[column] = height;
        }

        return key;
    }

    private boolean lockAndKeyFit(int[] lock, int[] key) {
        for (int i = 0; i < key.length; i++) {
            if (lock[i] + key[i] > LOCK_HEIGHT) {
                return false;
            }
        }

        return true;
    }
}
