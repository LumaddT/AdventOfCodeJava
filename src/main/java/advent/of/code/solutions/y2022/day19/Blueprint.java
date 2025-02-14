package advent.of.code.solutions.y2022.day19;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Blueprint {
    private final int[][] Costs;
    private final int[] BuildLimits;

    public Blueprint(String inputString) {
        Costs = new int[Minerals.values().length][Minerals.values().length];
        for (Minerals robot : Minerals.values()) {
            Arrays.fill(Costs[robot.ordinal()], 0);
        }

        String regex = "Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        if (matcher.find()) {
            Costs[Minerals.ORE.ordinal()][Minerals.ORE.ordinal()] = Integer.parseInt(matcher.group(1));
            Costs[Minerals.CLAY.ordinal()][Minerals.ORE.ordinal()] = Integer.parseInt(matcher.group(2));
            Costs[Minerals.OBSIDIAN.ordinal()][Minerals.ORE.ordinal()] = Integer.parseInt(matcher.group(3));
            Costs[Minerals.OBSIDIAN.ordinal()][Minerals.CLAY.ordinal()] = Integer.parseInt(matcher.group(4));
            Costs[Minerals.GEODE.ordinal()][Minerals.ORE.ordinal()] = Integer.parseInt(matcher.group(5));
            Costs[Minerals.GEODE.ordinal()][Minerals.OBSIDIAN.ordinal()] = Integer.parseInt(matcher.group(6));
        } else {
            throw new RuntimeException("Bad regex.");
        }

        BuildLimits = new int[Minerals.values().length];
        Arrays.fill(BuildLimits, 0);
        for (Minerals mineral : Minerals.values()) {
            for (Minerals robot : Minerals.values()) {
                if (Costs[robot.ordinal()][mineral.ordinal()] > BuildLimits[mineral.ordinal()]) {
                    BuildLimits[mineral.ordinal()] = Costs[robot.ordinal()][mineral.ordinal()];
                }
            }
        }

        BuildLimits[Minerals.GEODE.ordinal()] = Integer.MAX_VALUE;
    }

    public int calculateMaxOre(int timeRemaining) {
        int[] robotsOwned = new int[Minerals.values().length];
        Arrays.fill(robotsOwned, 0);
        int[] mineralsOwned = new int[Minerals.values().length];
        Arrays.fill(mineralsOwned, 0);
        boolean[] doNotBuild = new boolean[Minerals.values().length];
        Arrays.fill(doNotBuild, false);

        robotsOwned[Minerals.ORE.ordinal()] = 1;

        return calculateMaxOre(timeRemaining, robotsOwned, mineralsOwned, doNotBuild);
    }

    private int calculateMaxOre(int timeRemaining, int[] robotsOwned, int[] mineralsOwned, boolean[] doNotBuild) {
        if (timeRemaining == 0) {
            return 0;
        }

        boolean[] canBuild = new boolean[Minerals.values().length];
        Arrays.fill(canBuild, false);
        for (Minerals robot : Minerals.values()) {
            if (robotsOwned[robot.ordinal()] < BuildLimits[robot.ordinal()] && canBuild(robot, mineralsOwned)) {
                canBuild[robot.ordinal()] = true;
            }
        }

        boolean canBuildAll = true;
        for (Minerals robot : Minerals.values()) {
            if (!canBuild[robot.ordinal()] && robotsOwned[robot.ordinal()] < BuildLimits[robot.ordinal()]) {
                canBuildAll = false;
                break;
            }
        }

        int[] mineralsOwnedAfterMining = Arrays.copyOf(mineralsOwned, mineralsOwned.length);
        for (Minerals mineral : Minerals.values()) {
            mineralsOwnedAfterMining[mineral.ordinal()] += robotsOwned[mineral.ordinal()];
        }

        int returnValue = Integer.MIN_VALUE;

        if (!canBuildAll) {
            returnValue = calculateMaxOre(timeRemaining - 1, robotsOwned, mineralsOwnedAfterMining, canBuild);
        }

        for (Minerals mineral : Minerals.values()) {
            if (canBuild[mineral.ordinal()] && !doNotBuild[mineral.ordinal()]) {
                int[] newRobotsOwned = Arrays.copyOf(robotsOwned, robotsOwned.length);
                newRobotsOwned[mineral.ordinal()]++;

                int[] mineralsOwnedAfterConstruction = Arrays.copyOf(mineralsOwnedAfterMining, mineralsOwnedAfterMining.length);
                for (Minerals cost : Minerals.values()) {
                    mineralsOwnedAfterConstruction[cost.ordinal()] -= Costs[mineral.ordinal()][cost.ordinal()];
                }

                int tempMax = calculateMaxOre(timeRemaining - 1, newRobotsOwned, mineralsOwnedAfterConstruction, new boolean[Minerals.values().length]);

                returnValue = Math.max(returnValue, tempMax);
            }
        }

        return returnValue + robotsOwned[Minerals.GEODE.ordinal()];
    }

    private boolean canBuild(Minerals robot, int[] mineralsOwned) {
        for (Minerals mineral : Minerals.values()) {
            if (Costs[robot.ordinal()][mineral.ordinal()] > mineralsOwned[mineral.ordinal()]) {
                return false;
            }
        }

        return true;
    }
}
