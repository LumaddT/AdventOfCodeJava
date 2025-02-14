package advent.of.code.solutions.y2020.day04;

import advent.of.code.solutions.Day;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class Day04 implements Day {
    private static final Set<String> MANDATORY_FIELDS = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

    @Override
    public String part1(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n\n"))
                .filter(this::containsAllFields)
                .count());
    }

    @Override
    public String part2(String inputString) {
        return String.format("%d", Arrays.stream(inputString.split("\n\n"))
                .filter(this::allFieldsAreValid)
                .count());
    }

    private boolean containsAllFields(String document) {
        for (String field : MANDATORY_FIELDS) {
            if (!document.contains(field + ":")) {
                return false;
            }
        }

        return true;
    }

    private boolean allFieldsAreValid(String document) {
        String regex = "\\bbyr:(\\d{4})\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(document);

        if (!matcher.find() || Integer.parseInt(matcher.group(1)) < 1920 || Integer.parseInt(matcher.group(1)) > 2002) {
            return false;
        }

        regex = "\\biyr:(\\d{4})\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        if (!matcher.find() || Integer.parseInt(matcher.group(1)) < 2010 || Integer.parseInt(matcher.group(1)) > 2020) {
            return false;
        }

        regex = "\\beyr:(\\d{4})\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        if (!matcher.find() || Integer.parseInt(matcher.group(1)) < 2020 || Integer.parseInt(matcher.group(1)) > 2030) {
            return false;
        }

        regex = "\\bhgt:(\\d{2,3})(cm|in)\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        if (!matcher.find()) {
            return false;
        }

        int height = Integer.parseInt(matcher.group(1));

        if (matcher.group(2).equals("cm") && (height < 150 || height > 193)) {
            return false;
        } else if (matcher.group(2).equals("in") && (height < 59 || height > 76)) {
            return false;
        }

        regex = "\\bhcl:#[a-f\\d]{6}\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        if (!matcher.find()) {
            return false;
        }

        regex = "\\becl:(amb|blu|brn|gry|grn|hzl|oth)\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        if (!matcher.find()) {
            return false;
        }

        regex = "\\bpid:\\d{9}\\b";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(document);

        return matcher.find();
    }
}
