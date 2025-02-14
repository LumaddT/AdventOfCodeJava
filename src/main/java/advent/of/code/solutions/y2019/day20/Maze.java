package advent.of.code.solutions.y2019.day20;


import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ntuples.Tuple;

import java.util.*;

class Maze {
    private final char[][] Grid;
    private final Map<Coordinate, Warp> Warps;
    private Coordinate Entrance = null;
    private Coordinate Exit = null;
    private final Map<Travel, Integer> Distances;
    private final Map<Coordinate, List<Coordinate>> ValidDestinations;

    public Maze(String inputString) {
        String[] inputSplit = inputString.split("\n");
        int maxColumns = Arrays.stream(inputSplit).map(String::length).mapToInt(l -> l).max().orElseThrow();

        for (int row = 0; row < inputSplit.length; row++) {
            while (inputSplit[row].length() < maxColumns) {
                inputSplit[row] += ' ';
            }
        }

        Grid = Arrays.stream(inputSplit).map(String::toCharArray).toArray(char[][]::new);
        Warps = findWarps();

        for (int row = 0; row < Grid.length; row++) {
            for (int column = 0; column < Grid[row].length; column++) {
                if (Grid[row][column] != '.') {
                    Grid[row][column] = '#';
                }
            }
        }

        Tuple<Map<Travel, Integer>, Map<Coordinate, List<Coordinate>>> distancesAndValidDestinations = findDistancesAndValidDestinations();
        Distances = distancesAndValidDestinations.First();
        ValidDestinations = distancesAndValidDestinations.Second();
    }

    private Map<Coordinate, Warp> findWarps() {
        int holeTopRow = 2;
        int holeBottomRow = Grid.length - 3;
        int holeLeftmostColumn = 2;
        int holeRightmostColumn = Grid[0].length - 3;
        int middleRow = Grid.length / 2 + 1;
        int middleColumn = Grid[0].length / 2 + 1;

        while (Grid[holeTopRow][middleColumn] == '#' || Grid[holeTopRow][middleColumn] == '.') {
            holeTopRow++;
        }

        while (Grid[holeBottomRow][middleColumn] == '#' || Grid[holeBottomRow][middleColumn] == '.') {
            holeBottomRow--;
        }

        while (Grid[middleRow][holeLeftmostColumn] == '#' || Grid[middleRow][holeLeftmostColumn] == '.') {
            holeLeftmostColumn++;
        }

        while (Grid[middleRow][holeRightmostColumn] == '#' || Grid[middleRow][holeRightmostColumn] == '.') {
            holeRightmostColumn--;
        }

        Map<String, Coordinate> externalWarps = new HashMap<>();

        for (int column = 0; column < Grid[0].length; column++) {
            if (Character.isAlphabetic(Grid[0][column])) {
                String label = "" + Grid[0][column] + Grid[1][column];
                processLabel(label, 2, column, externalWarps);
            }
        }

        for (int column = 0; column < Grid[0].length; column++) {
            if (Character.isAlphabetic(Grid[Grid.length - 1][column])) {
                String label = "" + Grid[Grid.length - 2][column] + Grid[Grid.length - 1][column];
                processLabel(label, Grid.length - 3, column, externalWarps);
            }
        }

        for (int row = 0; row < Grid.length; row++) {
            if (Character.isAlphabetic(Grid[row][0])) {
                String label = "" + Grid[row][0] + Grid[row][1];
                processLabel(label, row, 2, externalWarps);
            }
        }

        for (int row = 0; row < Grid.length; row++) {
            if (Character.isAlphabetic(Grid[row][Grid[0].length - 1])) {
                String label = "" + Grid[row][Grid[0].length - 2] + Grid[row][Grid[0].length - 1];
                processLabel(label, row, Grid[0].length - 3, externalWarps);
            }
        }

        Map<String, Coordinate> internalWarps = new HashMap<>();

        for (int column = holeLeftmostColumn; column < holeRightmostColumn; column++) {
            if (Character.isAlphabetic(Grid[holeTopRow][column])) {
                String label = "" + Grid[holeTopRow][column] + Grid[holeTopRow + 1][column];
                processLabel(label, holeTopRow - 1, column, internalWarps);
            }
        }

        for (int column = holeLeftmostColumn; column < holeRightmostColumn; column++) {
            if (Character.isAlphabetic(Grid[holeBottomRow][column])) {
                String label = "" + Grid[holeBottomRow - 1][column] + Grid[holeBottomRow][column];
                processLabel(label, holeBottomRow + 1, column, internalWarps);
            }
        }

        for (int row = holeTopRow; row < holeBottomRow; row++) {
            if (Character.isAlphabetic(Grid[row][holeLeftmostColumn])) {
                String label = "" + Grid[row][holeLeftmostColumn] + Grid[row][holeLeftmostColumn + 1];
                processLabel(label, row, holeLeftmostColumn - 1, internalWarps);
            }
        }

        for (int row = holeTopRow; row < holeBottomRow; row++) {
            if (Character.isAlphabetic(Grid[row][holeRightmostColumn])) {
                String label = "" + Grid[row][holeRightmostColumn - 1] + Grid[row][holeRightmostColumn];
                processLabel(label, row, holeRightmostColumn + 1, internalWarps);
            }
        }


        if (!internalWarps.keySet().containsAll(externalWarps.keySet())) {
            throw new RuntimeException("Mismatch between internal and external warps.");
        }

        Map<Coordinate, Warp> warps = new HashMap<>();
        for (String label : internalWarps.keySet()) {
            Coordinate internal = internalWarps.get(label);
            Coordinate external = externalWarps.get(label);

            Warp warp = new Warp(external, internal);

            warps.put(internal, warp);
            warps.put(external, warp);
        }

        return warps;
    }

    private void processLabel(String label, int row, int column, Map<String, Coordinate> labelToCoordinate) {
        if (label.equals("AA")) {
            if (Entrance != null) {
                throw new RuntimeException("Bad label processing.");
            }

            Entrance = new Coordinate(row, column);
        } else if (label.equals("ZZ")) {
            if (Exit != null) {
                throw new RuntimeException("Bad label processing.");
            }

            Exit = new Coordinate(row, column);
        } else if (labelToCoordinate.containsKey(label)) {
            throw new RuntimeException("Bad warp location assumption");
        } else {
            labelToCoordinate.put(label, new Coordinate(row, column));
        }
    }

    private Tuple<Map<Travel, Integer>, Map<Coordinate, List<Coordinate>>> findDistancesAndValidDestinations() {
        Map<Travel, Integer> distances = new HashMap<>();
        Map<Coordinate, List<Coordinate>> validDestinations = new HashMap<>();

        Set<Coordinate> sources = new HashSet<>(Warps.keySet());
        sources.add(Entrance);

        Set<Coordinate> destinations = new HashSet<>(Warps.keySet());
        destinations.add(Exit);

        for (Coordinate source : sources) {
            for (Coordinate destination : destinations) {
                if (source.equals(destination)) {
                    continue;
                }

                Travel travel = new Travel(source, destination);
                Integer distance = calculateDistance(travel, false);
                if (distance != null) {
                    distances.put(travel, distance);
                    if (!validDestinations.containsKey(source)) {
                        validDestinations.put(source, new ArrayList<>());
                    }

                    validDestinations.get(source).add(destination);
                }
            }
        }

        return new Tuple<>(distances, validDestinations);
    }

    public int getDistanceEntranceToExitPart1() {
        Travel travel = new Travel(Entrance, Exit);
        Integer distance = calculateDistance(travel, true);

        if (distance == null) {
            throw new RuntimeException("Bad getDistanceEntranceToExitPart1");
        }

        return distance;
    }

    public int getDistanceEntranceToExitPart2() {
        return getDistanceEntranceToExitPart2(Entrance, 0, 0);
    }

    private int getDistanceEntranceToExitPart2(Coordinate currentCoordinate, int currentLayer, int recursiveStep) {
        if (recursiveStep > 130) {
            return Integer.MAX_VALUE;
        }

        List<Coordinate> validDestinations = new ArrayList<>(ValidDestinations.get(currentCoordinate));
        if (currentLayer != 0) {
            validDestinations.remove(Exit);
        }

        if (validDestinations.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        validDestinations.sort(this::compareCoordinates);

        int minDistance = Integer.MAX_VALUE;

        for (Coordinate destination : validDestinations) {
            Travel travel = new Travel(currentCoordinate, destination);
            int distance = Distances.get(travel);
            if (currentLayer == 0 && destination.equals(Exit)) {
                return distance;
            }

            int layer = currentLayer + Warps.get(destination).getDirection(destination);

            if (layer < 0) {
                continue;
            }

            int tmpDistance = 1 + distance + getDistanceEntranceToExitPart2(Warps.get(destination).getOther(destination), layer, recursiveStep + 1);
            if (tmpDistance < 0) {
                tmpDistance = Integer.MAX_VALUE;
            }

            minDistance = Math.min(minDistance, tmpDistance);
        }

        return minDistance;
    }

    private Integer calculateDistance(Travel travel, boolean allowWarp) {
        int[][] distances = new int[Grid.length][Grid[0].length];
        for (int row = 0; row < distances.length; row++) {
            for (int column = 0; column < distances[0].length; column++) {
                distances[row][column] = Integer.MAX_VALUE;
            }
        }

        Coordinate source = travel.Source();
        Coordinate destination = travel.Destination();

        distances[source.Row()][source.Column()] = 0;
        Queue<Coordinate> toCheck = new LinkedList<>();
        toCheck.add(source);

        while (!toCheck.isEmpty()) {
            Coordinate currentCoordinate = toCheck.poll();

            if (currentCoordinate.equals(destination)) {
                return distances[currentCoordinate.Row()][currentCoordinate.Column()];
            }

            List<Coordinate> adjacentCells = getAdjacentCells(currentCoordinate, allowWarp);
            int newDistance = distances[currentCoordinate.Row()][currentCoordinate.Column()] + 1;

            for (Coordinate adjacentCell : adjacentCells) {
                if (distances[adjacentCell.Row()][adjacentCell.Column()] > newDistance) {
                    distances[adjacentCell.Row()][adjacentCell.Column()] = newDistance;
                    toCheck.add(adjacentCell);
                }
            }
        }

        return null;
    }

    private List<Coordinate> getAdjacentCells(Coordinate coordinate, boolean allowWarp) {
        List<Coordinate> returnValue = new ArrayList<>();

        if (allowWarp && Warps.containsKey(coordinate)) {
            returnValue.add(Warps.get(coordinate).getOther(coordinate));
        }

        int row = coordinate.Row();
        int column = coordinate.Column();

        if (Grid[row - 1][column] == '.') {
            returnValue.add(new Coordinate(row - 1, column));
        }

        if (Grid[row + 1][column] == '.') {
            returnValue.add(new Coordinate(row + 1, column));
        }

        if (Grid[row][column - 1] == '.') {
            returnValue.add(new Coordinate(row, column - 1));
        }

        if (Grid[row][column + 1] == '.') {
            returnValue.add(new Coordinate(row, column + 1));
        }

        return Collections.unmodifiableList(returnValue);
    }

    private int compareCoordinates(Coordinate left, Coordinate right) {
        if (left.equals(this.Exit)) {
            return -1;
        }
        if (right.equals(Maze.this.Exit)) {
            return 1;
        }

        if (!this.Warps.containsKey(left) || !this.Warps.containsKey(right)) {
            throw new RuntimeException("Bad Coordinate comparison");
        }

        Warp thisWarp = this.Warps.get(left);
        if (thisWarp.getDirection(left) == -1) {
            return -1;
        }

        Warp otherWarp = this.Warps.get(right);
        if (otherWarp.getDirection(right) == -1) {
            return 1;
        }

        return 0;
    }
}
