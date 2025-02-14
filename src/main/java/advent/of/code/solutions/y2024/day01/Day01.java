package advent.of.code.solutions.y2024.day01;

import advent.of.code.solutions.Day;
import advent.of.code.utils.ntuples.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Day01 implements Day {
    @Override
    public String part1(String inputString) {
        Tuple<List<Integer>, List<Integer>> input = parseInputString(inputString);
        List<Integer> list1 = new ArrayList<>(input.First());
        List<Integer> list2 = new ArrayList<>(input.Second());

        Collections.sort(list1);
        Collections.sort(list2);

        int totalDistance = 0;

        for (int i = 0; i < list1.size(); i++) {
            totalDistance += Math.abs(list1.get(i) - list2.get(i));
        }

        return String.format("%d", totalDistance);
    }

    @Override
    public String part2(String inputString) {
        Tuple<List<Integer>, List<Integer>> input = parseInputString(inputString);
        List<Integer> list1 = new ArrayList<>(input.First());
        List<Integer> list2 = new ArrayList<>(input.Second());

        int similarityScore = 0;

        for (int location : list1) {
            similarityScore += location * (int) list2.stream().filter(l -> l.equals(location)).count();
        }

        return String.format("%d", similarityScore);
    }

    private Tuple<List<Integer>, List<Integer>> parseInputString(String inputString) {
        String[] inputSplit = inputString.split("\n");
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (String line : inputSplit) {
            String[] lineSplit = line.split(" +");

            list1.add(Integer.parseInt(lineSplit[0]));
            list2.add(Integer.parseInt(lineSplit[1]));
        }

        return new Tuple<>(Collections.unmodifiableList(list1), Collections.unmodifiableList(list2));
    }
}
