package advent.of.code.solutions.y2024.day16;

import advent.of.code.utils.DirectionUtils;
import advent.of.code.utils.Directions;

import java.util.*;

/**
 * If you're facing [direction], how far away is the end?
 */
class Distances {
    private final Map<Directions, Integer> BackingMap;

    // This is necessary for part 2, because otherwise it may count certain turns as free
    private final Set<Directions> ValidFacingDirections = new HashSet<>();

    public Distances(int value) {
        BackingMap = new HashMap<>();

        for (Directions direction : DirectionUtils.ALL_DIRECTIONS) {
            BackingMap.put(direction, value);
        }
    }

    public int get(Directions direction) {
        return BackingMap.get(direction);
    }

    public void put(Directions direction, int value) {
        BackingMap.put(direction, value);
    }

    public void addValidFacingDirection(Directions direction) {
        ValidFacingDirections.add(direction);
    }

    public boolean isValidFacingDirection(Directions direction) {
        return ValidFacingDirections.contains(direction);
    }

    public Set<Directions> getValidFacingDirections() {
        return Collections.unmodifiableSet(ValidFacingDirections);
    }
}
