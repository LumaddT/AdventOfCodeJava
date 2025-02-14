package advent.of.code.solutions.y2020.day02;

import advent.of.code.solutions.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day02 implements Day {
    @Override
    public String part1(String inputString) {
        List<Line> input = parseInputString(inputString);
        return String.format("%d", input.stream().filter(Line::isValidPart1).count());
    }

    @Override
    public String part2(String inputString) {
        List<Line> input = parseInputString(inputString);
        return String.format("%d", input.stream().filter(Line::isValidPart2).count());
    }

    private List<Line> parseInputString(String inputString) {
        List<Line> returnValue = new ArrayList<>();

        String regex = "(\\d+)-(\\d+)+ (.): ([^\\n]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        while (matcher.find()) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            char ch = matcher.group(3).charAt(0);
            String password = matcher.group(4);
            returnValue.add(new Line(min, max, ch, password));
        }

        return Collections.unmodifiableList(returnValue);
    }
}
