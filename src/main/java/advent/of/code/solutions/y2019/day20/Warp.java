package advent.of.code.solutions.y2019.day20;

import advent.of.code.utils.coordinates.Coordinate;

record Warp(Coordinate External, Coordinate Internal) {
    public Coordinate getOther(Coordinate coordinate) {
        if (External.equals(coordinate)) {
            return Internal;
        }

        if (Internal.equals(coordinate)) {
            return External;
        }

        throw new RuntimeException("Called getOther with bad Coordinate.");
    }

    /**
     * Returns the direction in which you go if you warp from coordinate
     */
    public int getDirection(Coordinate coordinate) {
        if (External.equals(coordinate)) {
            return -1;
        }

        if (Internal.equals(coordinate)) {
            return 1;
        }

        throw new RuntimeException("Called getDirection with bad Coordinate.");
    }
}
