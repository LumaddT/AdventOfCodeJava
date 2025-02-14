package advent.of.code.solutions.y2024.day03;

import advent.of.code.solutions.Day;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day03 implements Day {
    @Override
    public String part1(String inputString) {
        String regex = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        int total = 0;

        while (matcher.find()) {
            total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        String regex = "do\\(\\)|don't\\(\\)|mul\\(([0-9]{1,3}),([0-9]{1,3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        int total = 0;
        boolean enabled = true;

        while (matcher.find()) {
            if (matcher.group(0).equals("do()")) {
                enabled = true;
            } else if (matcher.group(0).equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                total += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }

        return String.format("%d", total);
    }
}
