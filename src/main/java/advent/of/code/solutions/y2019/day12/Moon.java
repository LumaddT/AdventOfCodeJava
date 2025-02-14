package advent.of.code.solutions.y2019.day12;

import advent.of.code.utils.RegexUtils;

import java.util.regex.Matcher;

class Moon {
    private final int[] Position = new int[3];
    private final int[] Velocity = new int[3];

    public Moon(String inputString) {
        String regex = "^<x=([-0-9]+), y=([-0-9]+), z=([-0-9]+)>$";
        Matcher matcher = RegexUtils.match(regex, inputString, 3);
        Position[0] = Integer.parseInt(matcher.group(1));
        Position[1] = Integer.parseInt(matcher.group(2));
        Position[2] = Integer.parseInt(matcher.group(3));

        Velocity[0] = 0;
        Velocity[1] = 0;
        Velocity[2] = 0;
    }

    public void move() {
        for (int i = 0; i < 3; i++) {
            Position[i] += Velocity[i];
        }
    }

    public void addToVelocity(int[] gravity) {
        if (gravity.length != 3) {
            throw new IllegalArgumentException(String.format("Bad gravity length %d. Expected: %d.", gravity.length, 3));
        }

        for (int i = 0; i < 3; i++) {
            Velocity[i] += gravity[i];
        }
    }

    public int getPotentialEnergy() {
        int returnValue = 0;

        for (int i = 0; i < 3; i++) {
            returnValue += Math.abs(Position[i]);
        }

        return returnValue;
    }

    public int getKineticEnergy() {
        int returnValue = 0;

        for (int i = 0; i < 3; i++) {
            returnValue += Math.abs(Velocity[i]);
        }

        return returnValue;
    }

    public int getPosition(int index) {
        if (index >= 3) {
            throw new IllegalArgumentException(String.format("Bad index %d.", index));
        }

        return Position[index];
    }

    public int getVelocity(int index) {
        if (index >= 3) {
            throw new IllegalArgumentException(String.format("Bad index %d.", index));
        }

        return Velocity[index];
    }
}
