package advent.of.code.utils.coordinates;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record Coordinate4(int X, int Y, int Z, int T) {
    public int distance(Coordinate4 other) {
        return Math.abs(this.X - other.X)
                + Math.abs(this.Y - other.Y)
                + Math.abs(this.Z - other.Z)
                + Math.abs(this.T - other.T);
    }

    public Set<Coordinate4> adjacent() {
        Set<Coordinate4> returnValue = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    for (int t = -1; t <= 1; t++) {
                        returnValue.add(new Coordinate4(X + x, Y + y, Z + z, T + t));
                    }
                }
            }
        }

        returnValue.remove(this);

        return Collections.unmodifiableSet(returnValue);
    }
}
