package advent.of.code.solutions.y2019.day18;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.coordinates.Coordinate;
import advent.of.code.utils.ParsingUtils;
import advent.of.code.utils.SetUtils;

import java.util.*;

/**
 * I feel like after the refactor this got a lot slower, probably because the use of boxed Characters
 * and the methods from ArrayUtils and SetUtils. It still takes a reasonable tim, so I'm keeping it.
 */
@SuppressWarnings("unused")
public class Day18 implements Day {
    @Override
    public String part1(String inputString) {
        Tunnels[] tunnelsArray = new Tunnels[]{new Tunnels(ParsingUtils.toGridBoxed(inputString))};
        Set<Character> missingKeys = getAllKeys(tunnelsArray);
        Character[] entrances = createEntrancesArray(tunnelsArray.length);

        int minSteps = calculateMinSteps(entrances, tunnelsArray, missingKeys, new HashMap<>());

        return String.format("%d", minSteps);
    }

    @Override
    public String part2(String inputString) {
        Tunnels[] tunnelsArray = parseInputStringPart2(inputString);
        Set<Character> missingKeys = getAllKeys(tunnelsArray);
        Character[] entrances = createEntrancesArray(tunnelsArray.length);

        int minSteps = calculateMinSteps(entrances, tunnelsArray, missingKeys, new HashMap<>());

        return String.format("%d", minSteps);
    }

    /**
     * In the examples there is one configuration that is not divided evenly in 4 quadrants, and it has the 4 labyrinths
     * going around each other. This parser does not account for that, as the actual puzzle input divides more easily.
     */
    private Tunnels[] parseInputStringPart2(String inputString) {
        Character[][] grid = ParsingUtils.toGridBoxed(inputString);
        replaceEntrance(grid);

        int halfRows = grid.length / 2;
        int halfColumns = grid[0].length / 2;
        Character[][][] subGrids = new Character[4][halfRows + 1][halfColumns + 1];

        for (int i = 0; i < 4; i++) {
            subGrids[i] = MatrixUtils.subSquare(grid, new Coordinate(halfRows * (i % 2), halfColumns * (i / 2)), halfRows + 1, halfColumns + 1);
        }

        return Arrays.stream(subGrids).map(Tunnels::new).toArray(Tunnels[]::new);
    }

    private void replaceEntrance(Character[][] grid) {
        Coordinate center = new Coordinate(grid.length / 2, grid[0].length / 2);

        if (MatrixUtils.getMatrixCoord(grid, center) != '@') {
            throw new RuntimeException("Entrance is not in the middle.");
        }

        for (Coordinate coordinate : SetUtils.difference(center.adjacent(), center.orthogonallyAdjacent())) {
            MatrixUtils.setMatrixCoord(grid, coordinate, '@');
        }

        for (Coordinate coordinate : center.orthogonallyAdjacent()) {
            MatrixUtils.setMatrixCoord(grid, coordinate, '#');
        }

        MatrixUtils.setMatrixCoord(grid, center, '#');
    }

    private Character[] createEntrancesArray(int length) {
        Character[] returnValue = new Character[length];
        Arrays.fill(returnValue, '@');
        return returnValue;
    }

    private Set<Character> getAllKeys(Tunnels[] tunnelsArray) {
        Set<Character> returnValue = new HashSet<>(tunnelsArray[0].getAllKeys());
        for (int i = 1; i < tunnelsArray.length; i++) {
            returnValue.addAll(tunnelsArray[i].getAllKeys());
        }

        return returnValue;
    }

    private int calculateMinSteps(Character[] currentLocations, Tunnels[] tunnels, Set<Character> missingKeys, HashMap<String, Integer> cache) {
        if (currentLocations.length != tunnels.length) {
            throw new RuntimeException("Locations array and tunnels array have different lengths.");
        }

        if (missingKeys.isEmpty()) {
            return 0;
        }

        String state = stateToString(currentLocations, missingKeys);
        if (cache.containsKey(state)) {
            return cache.get(state);
        }

        List<Set<Character>> reachableKeys = new ArrayList<>();
        for (int i = 0; i < tunnels.length; i++) {
            reachableKeys.add(tunnels[i].getReachableKeys(currentLocations[i], missingKeys));
        }

        int minSteps = Integer.MAX_VALUE;

        for (int i = 0; i < tunnels.length; i++) {
            for (char reachableKey : reachableKeys.get(i)) {
                if (!missingKeys.contains(reachableKey)) {
                    continue;
                }

                Set<Character> missingKeysClone = new HashSet<>(missingKeys);
                missingKeysClone.remove(reachableKey);

                Character[] newReachableKeys = Arrays.copyOf(currentLocations, currentLocations.length);
                newReachableKeys[i] = reachableKey;

                int tempSteps = tunnels[i].getDistance(currentLocations[i], reachableKey);
                tempSteps += calculateMinSteps(newReachableKeys, tunnels, missingKeysClone, cache);

                minSteps = Math.min(minSteps, tempSteps);
            }
        }

        cache.put(state, minSteps);
        return minSteps;
    }

    private String stateToString(Character[] currentLocations, Set<Character> missingKeys) {
        return String.join("|", Arrays.stream(currentLocations, 0, currentLocations.length).map(c -> "" + c).toList())
                + "||"
                + String.join("|", missingKeys.stream().sorted().map(c -> "" + c).toList());
    }
}
