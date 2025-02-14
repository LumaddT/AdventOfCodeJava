package advent.of.code.solutions.y2021.day13;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.SetUtils;
import advent.of.code.utils.ntuples.Tuple;
import advent.of.code.utils.screenParser.ScreenParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day13 implements Day {
    @Override
    public String part1(String inputString) {
        Tuple<Set<Coordinate>, List<Fold>> input = parseInputString(inputString);
        Set<Coordinate> visibleDots = new HashSet<>(input.First());
        List<Fold> folds = input.Second();

        return String.format("%d", applyFold(folds.getFirst(), visibleDots).size());
    }

    @Override
    public String part2(String inputString) {
        Tuple<Set<Coordinate>, List<Fold>> input = parseInputString(inputString);
        Set<Coordinate> visibleDots = new HashSet<>(input.First());
        List<Fold> folds = input.Second();

        for (Fold fold : folds) {
            visibleDots = applyFold(fold, visibleDots);
        }

        return ScreenParser.parseScreen(visibleDots);
    }

    private Tuple<Set<Coordinate>, List<Fold>> parseInputString(String inputString) {
        Set<Coordinate> dots = Arrays.stream(inputString.split("\n\n")[0].split("\n"))
                .map(l -> Arrays.stream(l.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray())
                .map(l -> new Coordinate(l[1], l[0]))
                .collect(Collectors.toSet());

        List<Fold> folds = Arrays.stream(inputString.split("\n\n")[1].split("\n"))
                .map(l -> l.split("="))
                .map(l -> new Fold(letterToOrientation(l[0].charAt(11)), Integer.parseInt(l[1])))
                .toList();

        return new Tuple<>(dots, folds);
    }

    private Orientation letterToOrientation(char ch) {
        return switch (ch) {
            case 'x' -> Orientation.VERTICAL;
            case 'y' -> Orientation.HORIZONTAL;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }

    private Set<Coordinate> applyFold(Fold fold, Set<Coordinate> visibleDots) {
        return switch (fold.orientation()) {
            case HORIZONTAL -> foldHorizontal(fold.position(), visibleDots);
            case VERTICAL -> foldVertical(fold.position(), visibleDots);
        };
    }

    private Set<Coordinate> foldHorizontal(int position, Set<Coordinate> visibleDots) {
        Set<Coordinate> dotsToFold = visibleDots.stream().filter(c -> c.Row() > position).collect(Collectors.toSet());

        visibleDots = SetUtils.difference(visibleDots, dotsToFold);

        for (Coordinate dot : dotsToFold) {
            visibleDots.add(new Coordinate(-dot.Row() + position * 2, dot.Column()));
        }

        return visibleDots;
    }

    private Set<Coordinate> foldVertical(int position, Set<Coordinate> visibleDots) {
        Set<Coordinate> dotsToFold = visibleDots.stream().filter(c -> c.Column() > position).collect(Collectors.toSet());

        visibleDots = SetUtils.difference(visibleDots, dotsToFold);

        for (Coordinate dot : dotsToFold) {
            visibleDots.add(new Coordinate(dot.Row(), -dot.Column() + position * 2));
        }

        return visibleDots;
    }
}
