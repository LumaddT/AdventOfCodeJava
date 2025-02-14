package advent.of.code.solutions.y2020.day19;

import advent.of.code.solutions.Day;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Day19 implements Day {
    @Override
    public String part1(String inputString) {
        Rule rule0 = parseRules(inputString);
        String[] strings = inputString.split("\n\n")[1].split("\n");

        int total = 0;
        Pattern pattern = Pattern.compile("^" + rule0.toRegexPart1() + "$");

        for (String string : strings) {
            if (pattern.matcher(string).find()) {
                total++;
            }
        }

        return String.format("%d", total);
    }

    @Override
    public String part2(String inputString) {
        Rule rule0 = parseRules(inputString);
        String[] strings = inputString.split("\n\n")[1].split("\n");

        int total = 0;
        Pattern pattern = Pattern.compile("^" + rule0.toRegexPart2() + "$");

        for (String string : strings) {
            if (pattern.matcher(string).find()) {
                total++;
            }
        }

        return String.format("%d", total);
    }

    private Rule parseRules(String inputString) {
        String[] ruleStrings = inputString.split("\n\n")[0].split("\n");
        Map<String, Rule> rules = new HashMap<>();

        for (String line : ruleStrings) {
            String[] splitLine = line.split(": ");
            rules.put(splitLine[0], new Rule(splitLine[0], splitLine[1].split(" ")));
        }

        for (String line : ruleStrings) {
            String[] splitLine = line.split(": ");
            String ruleId = splitLine[0];
            String[] subRules = splitLine[1].split(" ");

            for (String subRule : subRules) {
                if (rules.containsKey(subRule)) {
                    rules.get(ruleId).addSubRule(subRule, rules.get(subRule));
                }
            }
        }

        return rules.get("0");
    }
}
