package advent.of.code.solutions.y2021.day19;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate3;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day19 implements Day {
    @Override
    public String part1(String inputString) {
        List<Set<Coordinate3>> input = parseInputString(inputString);
        List<Scanner> scanners = input.stream().map(Scanner::new).toList();

        scanners.getFirst().setAsOrigin();

        findAllData(scanners);

        Set<Coordinate3> allBeacons = scanners.stream()
                .flatMap(s -> s.getBeaconsAbsoluteCoordinates().stream())
                .collect(Collectors.toSet());

        return String.format("%d", allBeacons.size());
    }

    @Override
    public String part2(String inputString) {
        List<Set<Coordinate3>> input = parseInputString(inputString);
        List<Scanner> scanners = input.stream().map(Scanner::new).toList();

        scanners.getFirst().setAsOrigin();

        findAllData(scanners);

        long maxDistance = Long.MIN_VALUE;

        for (int i = 0; i < scanners.size() - 1; i++) {
            for (int j = i + 1; j < scanners.size(); j++) {
                long candidateMaxDistance = scanners.get(i).getCoordinate().distance(scanners.get(j).getCoordinate());

                maxDistance = Math.max(maxDistance, candidateMaxDistance);
            }
        }

        return String.format("%d", maxDistance);
    }

    private List<Set<Coordinate3>> parseInputString(String inputString) {
        List<Set<Coordinate3>> returnValue = new ArrayList<>();

        for (String scannerString : inputString.split("(\n\n)?--- scanner \\d+ ---\n")) {
            if (scannerString.isEmpty()) {
                continue;
            }

            returnValue.add(Arrays.stream(scannerString.split("\n"))
                    .map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toArray(Integer[]::new))
                    .map(l -> new Coordinate3(l[0], l[1], l[2]))
                    .collect(Collectors.toSet()));
        }

        return Collections.unmodifiableList(returnValue);
    }

    private void findAllData(List<Scanner> scanners) {
        while (scanners.stream().anyMatch(s -> !s.isProcessed())) {
            for (int i = 0; i < scanners.size() - 1; i++) {
                for (int j = i + 1; j < scanners.size(); j++) {
                    if (scanners.get(i).isProcessed() == scanners.get(j).isProcessed()) {
                        continue;
                    }

                    Scanner knownScanner;
                    Scanner unknownScanner;
                    if (scanners.get(i).isProcessed()) {
                        knownScanner = scanners.get(i);
                        unknownScanner = scanners.get(j);
                    } else {
                        knownScanner = scanners.get(j);
                        unknownScanner = scanners.get(i);
                    }

                    unknownScanner.process(knownScanner);
                }
            }
        }
    }
}
