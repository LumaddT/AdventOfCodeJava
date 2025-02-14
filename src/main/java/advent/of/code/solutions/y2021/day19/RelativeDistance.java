package advent.of.code.solutions.y2021.day19;

import advent.of.code.utils.coordinates.Coordinate3;

record RelativeDistance(Coordinate3 firstBeacon, Coordinate3 secondBeacon,
                        long xDistance, long yDistance, long zDistance) {
    public boolean isSameDistance(RelativeDistance other) {
        return this.xDistance == other.xDistance
                && this.yDistance == other.yDistance
                && this.zDistance == other.zDistance;
    }
}
