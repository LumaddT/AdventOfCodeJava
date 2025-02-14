package advent.of.code.solutions.y2018.day22;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.RegexUtils;
import advent.of.code.utils.ntuples.Triple;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;

@SuppressWarnings("unused")
public class Day22 implements Day {
    @Override
    public String part1(String inputString) {
        Triple<Integer, Integer, Integer> input = parseInputString(inputString);

        int depth = input.First();
        int targetRow = input.Second();
        int targetColumn = input.Third();
        Coordinate targetCoordinate = new Coordinate(targetRow, targetColumn);

        Region firstRegionOfRow = new Region(targetCoordinate, depth);
        int riskLevel = 0;

        for (int row = 0; row <= targetRow; row++) {
            Region currentRegion = firstRegionOfRow;

            for (int column = 0; column <= targetColumn; column++) {
                riskLevel += currentRegion.getRiskLevel();
                currentRegion = currentRegion.getRight();
            }

            firstRegionOfRow = firstRegionOfRow.getDown();
        }

        return String.format("%d", riskLevel);
    }

    @Override
    public String part2(String inputString) {
        Triple<Integer, Integer, Integer> input = parseInputString(inputString);

        int depth = input.First();
        int targetRow = input.Second();
        int targetColumn = input.Third();
        Coordinate targetCoordinate = new Coordinate(targetRow, targetColumn);

        Region entryRegion = new Region(targetCoordinate, depth);

        PriorityQueue<Region> updateQueue = new PriorityQueue<>();
        updateQueue.add(entryRegion);
        Set<Region> regionsInQueue = new HashSet<>();
        regionsInQueue.add(entryRegion);

        while (true) {
            if (updateQueue.isEmpty()) {
                throw new RuntimeException("updateQueue is empty");
            }

            Region region = updateQueue.poll();
            regionsInQueue.remove(region);

            if (region.isTarget()) {
                return (String.format("%d", region.getTime(Gear.TORCH)));
            }

            for (Region destination : region.getNeighbors()) {
                if (region.updateTimes(destination) && !regionsInQueue.contains(destination)) {
                    regionsInQueue.add(destination);
                    updateQueue.add(destination);
                }
            }
        }
    }

    private Triple<Integer, Integer, Integer> parseInputString(String inputString) {
        Matcher matcher = RegexUtils.match("depth: (\\d+)\ntarget: ([\\d]+),(\\d+)", inputString, 3);
        int depth = Integer.parseInt(matcher.group(1));
        int targetColumn = Integer.parseInt(matcher.group(2));
        int targetRow = Integer.parseInt(matcher.group(3));

        return new Triple<>(depth, targetRow, targetColumn);
    }
}
