package advent.of.code.utils.coordinates;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record Coordinate3(long X, long Y, long Z) implements Comparable<Coordinate3> {
    public static final Coordinate3 ORIGIN = new Coordinate3(0, 0, 0);

    public long distance(Coordinate3 other) {
        return Math.abs(this.X - other.X)
                + Math.abs(this.Y - other.Y)
                + Math.abs(this.Z - other.Z);
    }

    public Set<Coordinate3> adjacent() {
        Set<Coordinate3> returnValue = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    returnValue.add(new Coordinate3(X + x, Y + y, Z + z));
                }
            }
        }

        returnValue.remove(this);

        return Collections.unmodifiableSet(returnValue);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive X) around the center of rotation.
     */
    public Coordinate3 rotateXAxis(Coordinate3 centerOfRotation) {
        long newY = -Z + centerOfRotation.Z + centerOfRotation.Y;
        long newZ = Y - centerOfRotation.Y + centerOfRotation.Z;
        return new Coordinate3(X, newY, newZ);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive Y) around the center of rotation.
     */
    public Coordinate3 rotateYAxis(Coordinate3 centerOfRotation) {
        long newX = Z - centerOfRotation.Z + centerOfRotation.X;
        long newZ = -X + centerOfRotation.X + centerOfRotation.Z;
        return new Coordinate3(newX, Y, newZ);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive Z) around the center of rotation.
     */
    public Coordinate3 rotateZAxis(Coordinate3 centerOfRotation) {
        long newX = -Y + centerOfRotation.Y + centerOfRotation.X;
        long newY = X - centerOfRotation.X + centerOfRotation.Y;
        return new Coordinate3(newX, newY, Z);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive X) around the center of rotation.
     */
    public Coordinate3 rotateXAxis() {
        return rotateXAxis(ORIGIN);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive Y) around the center of rotation.
     */
    public Coordinate3 rotateYAxis() {
        return rotateYAxis(ORIGIN);
    }

    /**
     * Rotates the point clockwise (looking from the center of rotation to positive Z) around the center of rotation.
     */
    public Coordinate3 rotateZAxis() {
        return rotateZAxis(ORIGIN);
    }

    @Override
    public int compareTo(Coordinate3 other) {
        if (X != other.X) {
            return (int) (X - other.X);
        }

        if (Y != other.Y) {
            return (int) (Y - other.Y);
        }

        return (int) (Z - other.Z);
    }

    public Coordinate3 add(Coordinate3 other) {
        return new Coordinate3(X + other.X, Y + other.Y, Z + other.Z);
    }

    public Coordinate3 subtract(Coordinate3 other) {
        return new Coordinate3(X - other.X, Y - other.Y, Z - other.Z);
    }
}
