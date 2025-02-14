package advent.of.code.solutions.y2021.day19;

import advent.of.code.utils.SetUtils;
import advent.of.code.utils.coordinates.Coordinate3;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

class Scanner {
    private static final int NECESSARY_IN_COMMON = 12;

    private final List<Set<Coordinate3>> AllRotations;
    private final List<Set<RelativeDistance>> RelativeDistances;

    @Getter
    private Coordinate3 Coordinate = null;
    private Set<RelativeDistance> MatchedRelativeDistances = null;
    private Set<Coordinate3> BeaconsAbsoluteCoordinates = null;

    public Scanner(Set<Coordinate3> beacons) {
        AllRotations = getAllRotations(beacons);
        RelativeDistances = AllRotations.stream().map(this::calculateRelativeDistances).toList();
    }

    public void setAbsoluteData(Coordinate3 coordinate, Set<Coordinate3> matchedRotation, Set<RelativeDistance> matchedRelativeDistances) {
        if (Coordinate != null) {
            throw new RuntimeException("Bad assignment of absolute data to Scanner.");
        }

        Coordinate = coordinate;
        MatchedRelativeDistances = matchedRelativeDistances;
        BeaconsAbsoluteCoordinates = matchedRotation.stream().map(c -> c.add(Coordinate)).collect(Collectors.toSet());
    }

    public void setAsOrigin() {
        setAbsoluteData(Coordinate3.ORIGIN, AllRotations.getFirst(), RelativeDistances.getFirst());
    }

    private List<Set<Coordinate3>> getAllRotations(Set<Coordinate3> coordinates) {
        // I think there is a way to do this more algorithmically applying the same steps
        // to a point iteratively, but this works too.
        return List.of(Collections.unmodifiableSet(coordinates),
                coordinates.stream().map(c -> c.rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(Coordinate3::rotateXAxis).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(Coordinate3::rotateXAxis).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),

                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet()),
                coordinates.stream().map(c -> c.rotateYAxis().rotateYAxis()).map(c -> c.rotateXAxis().rotateXAxis().rotateXAxis().rotateYAxis().rotateXAxis().rotateYAxis().rotateXAxis()).collect(Collectors.toSet())
        );
    }

    private Set<RelativeDistance> calculateRelativeDistances(Set<Coordinate3> beacons) {
        Set<RelativeDistance> relativeDistances = new HashSet<>();
        List<Coordinate3> beaconsList = new ArrayList<>(beacons);

        for (int i = 0; i < beaconsList.size() - 1; i++) {
            for (int j = i + 1; j < beaconsList.size(); j++) {
                Coordinate3 first = beaconsList.get(i);
                Coordinate3 second = beaconsList.get(j);

                if (first.compareTo(second) > 0) {
                    Coordinate3 tmp = first;
                    first = second;
                    second = tmp;
                }

                relativeDistances.add(new RelativeDistance(first, second,
                        first.X() - second.X(),
                        first.Y() - second.Y(),
                        first.Z() - second.Z()));
            }
        }

        return Collections.unmodifiableSet(relativeDistances);
    }


    public Set<Coordinate3> getBeaconsAbsoluteCoordinates() {
        if (BeaconsAbsoluteCoordinates == null) {
            throw new RuntimeException("Bad call to getAbsoluteCoordinates().");
        }

        return BeaconsAbsoluteCoordinates;
    }

    public Set<RelativeDistance> getMatchedRelativeDistances() {
        if (MatchedRelativeDistances == null) {
            throw new RuntimeException("Bad call to getMatchedRelativeDistances().");
        }

        return MatchedRelativeDistances;
    }

    public boolean isProcessed() {
        return BeaconsAbsoluteCoordinates != null;
    }

    public void process(Scanner knownScanner) {
        Set<RelativeDistance> knownRelativeDistances = knownScanner.getMatchedRelativeDistances();
        for (int i = 0; i < this.RelativeDistances.size(); i++) {
            Set<RelativeDistance> unknownRelativeDistances = this.RelativeDistances.get(i);

            List<RelativeDistance> inCommonKnown = new ArrayList<>();
            List<RelativeDistance> inCommonUnknown = new ArrayList<>();

            for (RelativeDistance knownRelativeDistance : knownRelativeDistances) {
                for (RelativeDistance unknownRelativeDistance : unknownRelativeDistances) {
                    if (knownRelativeDistance.isSameDistance(unknownRelativeDistance)) {
                        inCommonKnown.add(knownRelativeDistance);
                        inCommonUnknown.add(unknownRelativeDistance);
                    }
                }
            }

            if (inCommonKnown.size() >= NECESSARY_IN_COMMON * (NECESSARY_IN_COMMON - 1) / 2) {
                Set<Coordinate3> matchedRotation = this.AllRotations.get(i);
                RelativeDistance knownReference = inCommonKnown.stream().findAny().orElseThrow();
                if (inCommonKnown.stream().filter(d -> d.isSameDistance(knownReference)).count() != 1) {
                    throw new RuntimeException("Found 2 RelativeDistance that are the same in the same set.");
                }

                RelativeDistance unknownReference = inCommonUnknown.stream()
                        .filter(d -> d.isSameDistance(knownReference))
                        .findAny().orElseThrow();

                Coordinate3 potentialScannerCoordinate = knownReference.firstBeacon().subtract(unknownReference.firstBeacon().subtract(knownScanner.Coordinate));

                Set<Coordinate3> potentialAbsoluteCoordinates = matchedRotation.stream().map(c -> c.add(potentialScannerCoordinate)).collect(Collectors.toSet());

                Set<Coordinate3> potentialAbsoluteCoordinatesInCommon = SetUtils.intersection(potentialAbsoluteCoordinates, knownScanner.getBeaconsAbsoluteCoordinates());

                if (potentialAbsoluteCoordinatesInCommon.size() >= NECESSARY_IN_COMMON) {
                    this.setAbsoluteData(potentialScannerCoordinate, matchedRotation, unknownRelativeDistances);
                    return;
                }
            }
        }
    }
}
