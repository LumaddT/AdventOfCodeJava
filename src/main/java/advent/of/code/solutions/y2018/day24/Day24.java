package advent.of.code.solutions.y2018.day24;

import advent.of.code.solutions.Day;
import advent.of.code.utils.RegexUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day24 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Group> groups = parseInputString(inputString);
        performAllFights(groups);

        int returnValue = 0;

        for (Group group : groups) {
            returnValue += group.getUnits();
        }

        return String.format("%d", returnValue);
    }

    @Override
    public String part2(String inputString) {
        int minBoostUpperLimit = 1_000_000;
        int minBoostLowerLimit = 0;
        int unitsAliveUpperLimit = -1;

        while (minBoostUpperLimit != minBoostLowerLimit) {
            int testValue = ((minBoostUpperLimit - minBoostLowerLimit) / 2) + minBoostLowerLimit;

            Set<Group> groups = parseInputString(inputString);
            for (Group group : groups) {
                if (group.getSide() == Sides.ImmuneSystem) {
                    group.applyBoost(testValue);
                }
            }

            Sides winner = performAllFights(groups);

            if (winner == Sides.ImmuneSystem) {
                minBoostUpperLimit = testValue;
                unitsAliveUpperLimit = 0;
                for (Group group : groups) {
                    unitsAliveUpperLimit += group.getUnits();
                }


            } else {
                minBoostLowerLimit = testValue + 1;
            }
        }

        return String.format("%d", unitsAliveUpperLimit);
    }

    private Sides performAllFights(Set<Group> groups) {
        int immuneSystemAlive = (int) groups.stream().filter(g -> g.getSide() == Sides.ImmuneSystem).count();
        int infectionAlive = (int) groups.stream().filter(g -> g.getSide() == Sides.Infection).count();

        while (immuneSystemAlive > 0 && infectionAlive > 0) {
            Set<Group> groupsNotSelected = new HashSet<>(groups);
            Map<Group, Group> matches = new HashMap<>();

            for (Group group : groups.stream().sorted(Group::compareToSelection).toList()) {
                Group target = chooseTarget(group, groupsNotSelected);
                if (target != null) {
                    groupsNotSelected.remove(target);
                    matches.put(group, target);
                }
            }

            boolean isStalemate = true;
            for (Group attacker : groups.stream().sorted(Group::compareToAttack).toList()) {
                if (attacker.isAlive() && matches.containsKey(attacker)) {
                    if (attacker.attack(matches.get(attacker))) {
                        isStalemate = false;
                    }
                }
            }

            if (isStalemate) {
                return Sides.Infection;
            }

            for (Group group : groups) {
                if (!group.isAlive()) {
                    if (group.getSide() == Sides.ImmuneSystem) {
                        immuneSystemAlive--;
                    } else {
                        infectionAlive--;
                    }
                }
            }

            groups.removeIf(group -> !group.isAlive());
        }

        return groups.stream().findAny().orElseThrow().getSide();
    }

    private Set<Group> parseInputString(String inputString) {
        String[][] inputSplit = Arrays.stream(inputString.split("\n\n")).map(i -> i.split("\n")).toArray(String[][]::new);

        Set<Group> returnValue = new HashSet<>();

        if (inputSplit.length != 2 || !inputSplit[0][0].equals("Immune System:") || !inputSplit[1][0].equals("Infection:")) {
            throw new RuntimeException("Bad input file.");
        }

        for (int i = 1; i < inputSplit[0].length; i++) {
            returnValue.add(parseLine(inputSplit[0][i], Sides.ImmuneSystem));
        }

        for (int i = 1; i < inputSplit[1].length; i++) {
            returnValue.add(parseLine(inputSplit[1][i], Sides.Infection));
        }

        return returnValue;
    }

    private Group parseLine(String line, Sides side) {
        String baseDataRegex = "^(\\d+) units each with (\\d+) hit points(?: \\(.*\\))? with an attack that does (\\d+) ([a-z]+) damage at initiative (\\d+)$";
        Matcher match = RegexUtils.match(baseDataRegex, line, 5);

        int units = Integer.parseInt(match.group(1));
        int hitPoints = Integer.parseInt(match.group(2));
        int attackDamage = Integer.parseInt(match.group(3));
        String attackType = match.group(4);
        int initiative = Integer.parseInt(match.group(5));
        Set<String> immunities = new HashSet<>();
        Set<String> weaknesses = new HashSet<>();

        if (line.contains("immune to")) {
            match = RegexUtils.match("^.+?immune to ([a-z, ]+)[;)].+?$", line, 1);
            immunities = Arrays.stream(match.group(1).split(", ")).collect(Collectors.toSet());
        }

        if (line.contains("weak to")) {
            match = RegexUtils.match("^.+?weak to ([a-z, ]+)[;)].+?$", line, 1);
            weaknesses = Arrays.stream(match.group(1).split(", ")).collect(Collectors.toSet());
        }

        return new Group(units, hitPoints, attackDamage, attackType, initiative, immunities, weaknesses, side);
    }

    private Group chooseTarget(Group attacker, Set<Group> possibleTargets) {
        Group returnValue = null;
        int maxPotentialDamage = Integer.MIN_VALUE;

        for (Group possibleTarget : possibleTargets) {
            int potentialDamage = attacker.damageTo(possibleTarget);

            if (potentialDamage == 0) {
                continue;
            }

            //noinspection DataFlowIssue
            if (potentialDamage > maxPotentialDamage
                    || potentialDamage == maxPotentialDamage && possibleTarget.isBetterTargetThan(returnValue)) {
                maxPotentialDamage = potentialDamage;
                returnValue = possibleTarget;
            }
        }

        return returnValue;
    }
}
