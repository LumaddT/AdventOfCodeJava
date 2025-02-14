package advent.of.code.solutions.y2019.day18;

import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.SetUtils;
import advent.of.code.utils.ntuples.Tuple;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

class Tunnels {
    private final Character[][] Grid;
    private final Map<Character, Coordinate> PointsOfInterest;
    private final Map<Travel, Integer> Distances;
    private final Map<Travel, Set<Character>> KeysRequiredToReach;
    @Getter
    private final Set<Character> AllKeys;

    public Tunnels(Character[][] grid) {
        Grid = grid;

        Map<Character, Coordinate> pointsOfInterest = new HashMap<>();
        for (int row = 0; row < Grid.length; row++) {
            for (int column = 0; column < Grid[row].length; column++) {
                if (Grid[row][column] >= 'a' && grid[row][column] <= 'z' || grid[row][column] == '@') {
                    pointsOfInterest.put(Grid[row][column], new Coordinate(row, column));
                }
            }
        }
        PointsOfInterest = Collections.unmodifiableMap(pointsOfInterest);

        Tuple<Map<Travel, Integer>, Map<Travel, Set<Character>>> distancesKeysRequired = findDistancesAndKeys();
        Distances = distancesKeysRequired.First();
        KeysRequiredToReach = distancesKeysRequired.Second();

        AllKeys = PointsOfInterest.keySet().stream().filter(k -> k != '@').collect(Collectors.toSet());
    }

    private Tuple<Map<Travel, Integer>, Map<Travel, Set<Character>>> findDistancesAndKeys() {
        Map<Travel, Integer> distances = new HashMap<>();
        Map<Travel, Set<Character>> keysRequired = new HashMap<>();

        for (char source : PointsOfInterest.keySet()) {
            for (char destination : PointsOfInterest.keySet()) {
                if (source == destination) {
                    continue;
                }

                Travel travel = new Travel(source, destination);
                Tuple<Integer, Set<Character>> distanceKeys = calculateDistanceKeys(source, destination);
                distances.put(travel, distanceKeys.First());
                keysRequired.put(travel, distanceKeys.Second());
            }
        }

        return new Tuple<>(Collections.unmodifiableMap(distances), Collections.unmodifiableMap(keysRequired));
    }

    private Tuple<Integer, Set<Character>> calculateDistanceKeys(char source, char destination) {
        Coordinate sourceCoord = PointsOfInterest.get(source);
        Coordinate destinationCoord = PointsOfInterest.get(destination);

        Integer[][] distanceGrid = new Integer[Grid.length][Grid[0].length];
        MatrixUtils.fillMatrix(distanceGrid, Integer.MAX_VALUE);
        distanceGrid[sourceCoord.Row()][sourceCoord.Column()] = 0;

        Queue<Coordinate> toCheckQueue = new LinkedList<>();
        toCheckQueue.add(sourceCoord);

        int distance;

        while (true) {
            if (toCheckQueue.isEmpty()) {
                throw new RuntimeException("Bad calculateDistanceKey implementation.");
            }

            Coordinate toCheck = toCheckQueue.poll();

            if (toCheck.equals(destinationCoord)) {
                distance = MatrixUtils.getMatrixCoord(distanceGrid, toCheck);
                break;
            }

            int nextDistance = MatrixUtils.getMatrixCoord(distanceGrid, toCheck) + 1;

            for (Coordinate adjacent : toCheck.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(Grid, adjacent) != '#' && MatrixUtils.getMatrixCoord(distanceGrid, adjacent) > nextDistance) {
                    MatrixUtils.setMatrixCoord(distanceGrid, adjacent, nextDistance);
                    toCheckQueue.add(adjacent);
                }
            }
        }

        Set<Character> keysRequired = new HashSet<>();

        Coordinate currentCoords = destinationCoord;
        while (!currentCoords.equals(sourceCoord)) {
            char currentTile = MatrixUtils.getMatrixCoord(Grid, currentCoords);
            if (currentTile >= 'A' && currentTile <= 'Z') {
                keysRequired.add(Character.toLowerCase(currentTile));
            }

            for (Coordinate adjacent : currentCoords.orthogonallyAdjacent()) {
                if (MatrixUtils.getMatrixCoord(distanceGrid, adjacent) < MatrixUtils.getMatrixCoord(distanceGrid, currentCoords)) {
                    currentCoords = adjacent;
                    break;
                }
            }
        }

        return new Tuple<>(distance, Collections.unmodifiableSet(keysRequired));
    }

    public Set<Character> getReachableKeys(char currentLocation, Set<Character> missingKeys) {
        Set<Character> returnValue = new HashSet<>();

        for (char possibleDestination : AllKeys) {
            if (possibleDestination == currentLocation) {
                continue;
            }

            Travel travel = new Travel(currentLocation, possibleDestination);
            Set<Character> keysRequired = KeysRequiredToReach.get(travel);

            if (SetUtils.intersection(missingKeys, keysRequired).isEmpty()) {
                returnValue.add(possibleDestination);
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    public int getDistance(char source, char destination) {
        return Distances.get(new Travel(source, destination));
    }
}
