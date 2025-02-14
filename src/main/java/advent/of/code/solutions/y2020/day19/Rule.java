package advent.of.code.solutions.y2020.day19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Rule {
    private final String RuleId;
    private final String[] RuleStrings;

    private final Map<String, Rule> SubRules = new HashMap<>();

    public Rule(String ruleId, String[] ruleStrings) {
        RuleId = ruleId;
        RuleStrings = Arrays.stream(ruleStrings).map(r -> r.replace("\"", "")).toArray(String[]::new);
    }

    public void addSubRule(String id, Rule subRule) {
        SubRules.put(id, subRule);
    }

    public String toRegexPart1() {
        StringBuilder returnValue = new StringBuilder();

        for (String ruleString : RuleStrings) {
            if (SubRules.containsKey(ruleString)) {
                returnValue.append('(');
                returnValue.append(SubRules.get(ruleString).toRegexPart1());
                returnValue.append(')');
            } else {
                returnValue.append(ruleString);
            }
        }

        return returnValue.toString();
    }

    public String toRegexPart2() {
        StringBuilder returnValue = new StringBuilder();

        // So incredibly hacky but it works. I'll take it.
        if (RuleId.equals("11")) {
            for (int i = 1; i < 10; i++) {
                returnValue.append(String.format("(%s){%d}(%s){%d}", SubRules.get("42").toRegexPart2(), i, SubRules.get("31").toRegexPart2(), i));
                returnValue.append("|");
            }

            returnValue.append(String.format("(%s){%d}(%s){%d}", SubRules.get("42"), 10, SubRules.get("31"), 10));

            return returnValue.toString();
        }

        for (String ruleString : RuleStrings) {
            if (SubRules.containsKey(ruleString)) {
                returnValue.append('(');
                returnValue.append(SubRules.get(ruleString).toRegexPart2());
                returnValue.append(')');

                if (RuleId.equals("8")) {
                    returnValue.append('+');
                }
            } else {
                returnValue.append(ruleString);
            }
        }

        return returnValue.toString();
    }
}
