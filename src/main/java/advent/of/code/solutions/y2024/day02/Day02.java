package advent.of.code.solutions.y2024.day02;

import advent.of.code.solutions.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day02 implements Day {
    private static final int MIN_DIFF = 1;
    private static final int MAX_DIFF = 3;

    @Override
    public String part1(String inputString) {
        Set<List<Integer>> input = parseInputString(inputString);
        return String.format("%d", input.stream().filter(this::isSafe).count());
    }

    @Override
    public String part2(String inputString) {
        Set<List<Integer>> input = parseInputString(inputString);
        return String.format("%d", input.stream().filter(this::isSafeCanSkip1).count());
    }

    private Set<List<Integer>> parseInputString(String inputString) {
        return Arrays.stream(inputString.split("\n"))
                .map(l -> Arrays.stream(l.split(" "))
                        .map(Integer::parseInt)
                        .toList())
                .collect(Collectors.toSet());
    }

    private boolean isSafe(List<Integer> report) {
        if (!isAllAscending(report) && !isAllDescending(report)) {
            return false;
        }

        return respectsDiffs(report);
    }

    private boolean isAllAscending(List<Integer> report) {
        for (int i = 1; i < report.size(); i++) {
            if (report.get(i - 1) < report.get(i)) {
                return false;
            }
        }

        return true;
    }

    private boolean isAllDescending(List<Integer> report) {
        for (int i = 1; i < report.size(); i++) {
            if (report.get(i - 1) > report.get(i)) {
                return false;
            }
        }

        return true;
    }

    private boolean respectsDiffs(List<Integer> report) {
        for (int i = 1; i < report.size(); i++) {
            int diff = Math.abs(report.get(i - 1) - report.get(i));
            if (diff < MIN_DIFF || diff > MAX_DIFF) {
                return false;
            }
        }

        return true;
    }

    private boolean isSafeCanSkip1(List<Integer> report) {
        for (int i = 0; i < report.size(); i++) {
            List<Integer> reportCopy = new ArrayList<>(report);
            //noinspection SuspiciousListRemoveInLoop
            reportCopy.remove(i);
            if (isSafe(reportCopy)) {
                return true;
            }
        }

        return false;
    }
}
