package advent.of.code;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final int YEAR = 2025;
    private static final int DAY = 1;
    private static final int PART = 0;
    private static final boolean TEST_RUN = false;

    private static final boolean VERIFY_ALL = false;
    private static final boolean DO_NOT_REPORT_CORRECT_SOLUTIONS = false;
    private static final int MARK_LONGER_THAN_MS = 0;
    private static final int FIRST_YEAR = 2015;
    private static final int LAST_YEAR = 2025;

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        if (VERIFY_ALL) {
            verifyAllSolutions();
            return;
        }

        Day dayClass = getDayClass(YEAR, DAY);
        if (dayClass == null) {
            throw new RuntimeException("Could not find solution class for y%dd%d.".formatted(YEAR, DAY));
        }

        String inputString = getInputString(YEAR, DAY, TEST_RUN);
        if (inputString == null) {
            throw new RuntimeException("Could not find input for y%dd%d.".formatted(YEAR, DAY));
        }

        System.out.printf("Year %d, day %02d:%n", YEAR, DAY);

        if (PART == 1) {
            System.out.printf("Part 1: %s%n", dayClass.part1(inputString));
        } else if (PART == 2) {
            System.out.printf("Part 2: %s%n", dayClass.part2(inputString));
        } else if (PART == 0) {
            System.out.printf("Part 1: %s%n", dayClass.part1(inputString));
            System.out.printf("Part 2: %s%n", dayClass.part2(inputString));
        } else {
            System.out.printf("Illegal part number %d.%n", PART);
        }
    }

    private static void verifyAllSolutions() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        double totalTime = 0;

        for (int year = FIRST_YEAR; year <= LAST_YEAR; year++) {
            for (int day = 1; day <= 25; day++) {
                totalTime += verifySingleSolution(year, day);
            }
        }

        System.out.printf("Total time: %f ms.%n", totalTime);
    }

    private static double verifySingleSolution(int year, int day) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Day dayClass = getDayClass(year, day);
        if (dayClass == null) {
            return 0;
        }

        String inputString = getInputString(year, day, false);
        if (inputString == null) {
            throw new RuntimeException("Could not find input for y%dd%d.".formatted(year, day));
        }

        Tuple<String, String> expectedAnswers = getExpectedAnswers(year, day);
        if (expectedAnswers == null) {
            throw new RuntimeException("Could not find expected answer file for y%dd%d, or the file is not well formatted.".formatted(year, day));
        }

        String part1expected = expectedAnswers.First();
        String part2expected = expectedAnswers.Second();

        long part1StartTime = System.nanoTime();
        String part1calculated = dayClass.part1(inputString);
        long part1EndTime = System.nanoTime();

        double part1MsDuration = ((double) (part1EndTime - part1StartTime)) / 1_000_000;

        if (!part1calculated.equals(part1expected)) {
            System.out.printf("year %d, day %02d, part1: Expected: %s, calculated: %s.%n", year, day, part1expected, part1calculated);
        } else if (part1MsDuration > MARK_LONGER_THAN_MS && !DO_NOT_REPORT_CORRECT_SOLUTIONS) {
            System.out.printf("year %d, day %02d, part1: Good. Time: %f ms.%n", year, day, part1MsDuration);
        }

        long part2StartTime = System.nanoTime();
        String part2calculated = dayClass.part2(inputString);
        long part2EndTime = System.nanoTime();

        double part2MsDuration = ((double) (part2EndTime - part2StartTime)) / 1_000_000;

        if (!part2calculated.equals(part2expected)) {
            System.out.printf("year %d, day %02d, part2: Expected: %s, calculated: %s.%n", year, day, part2expected, part2calculated);
        } else if (part2MsDuration > MARK_LONGER_THAN_MS && !DO_NOT_REPORT_CORRECT_SOLUTIONS) {
            System.out.printf("year %d, day %02d, part2: Good. Time: %f ms.%n", year, day, part2MsDuration);
        }

        return part1MsDuration + part2MsDuration;
    }

    private static String getInputString(int year, int day, boolean testRun) throws IOException {
        String inputFilePathString = "inputs" + File.separator
                + (testRun ? ("tests" + File.separator) : "")
                + year + File.separator
                + String.format("%02d", day) + ".txt";
        Path inputFilePath = Paths.get(inputFilePathString);

        if (!Files.exists(inputFilePath)) {
            System.out.println("Input file not found.");
            return null;
        }

        return Files.readString(inputFilePath);
    }

    private static Day getDayClass(int year, int day) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            Class<?> solutionClass = Class.forName(String.format("advent.of.code.solutions.y%d.day%02d.Day%02d", year, day, day));
            return (Day) solutionClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Tuple<String, String> getExpectedAnswers(int year, int day) throws IOException {
        String answerFilePathString = "answers" + File.separator
                + year + File.separator
                + String.format("%02d", day) + ".txt";
        Path answerFilePath = Paths.get(answerFilePathString);

        if (!Files.exists(answerFilePath)) {
            System.out.printf("Answer file not found for year %d, day %d.%n", year, day);
            return null;
        }

        String[] expectedAnswers = Files.readString(answerFilePath).split("\n");

        if (expectedAnswers.length != 2) {
            return null;
        }

        return new Tuple<>(expectedAnswers[0], expectedAnswers[1]);
    }
}
