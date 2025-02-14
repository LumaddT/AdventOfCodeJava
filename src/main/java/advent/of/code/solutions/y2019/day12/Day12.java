package advent.of.code.solutions.y2019.day12;

import advent.of.code.solutions.Day;

import java.util.*;

@SuppressWarnings("unused")
public class Day12 implements Day {
    private static final int ITERATIONS_PART_1 = 1_000;

    @Override
    public String part1(String inputString) {
        Moon[] moons = Arrays.stream(inputString.split("\n")).map(Moon::new).toArray(Moon[]::new);

        for (int i = 0; i < ITERATIONS_PART_1; i++) {
            simulateTick(moons);
        }

        return String.format("%d", Arrays.stream(moons)
                .mapToInt(m -> m.getPotentialEnergy() * m.getKineticEnergy())
                .sum()
        );
    }

    @Override
    public String part2(String inputString) {
        Moon[] moons = Arrays.stream(inputString.split("\n")).map(Moon::new).toArray(Moon[]::new);

        List<List<AxisState>> axisStatesSorted = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            axisStatesSorted.add(new ArrayList<>());
        }

        // Much faster .contains() operation
        List<Set<AxisState>> axisStatesUnsorted = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            axisStatesUnsorted.add(new HashSet<>());
        }

        for (int i = 0; i < 3; i++) {
            AxisState axisState = new AxisState(
                    moons[0].getPosition(i), moons[0].getVelocity(i),
                    moons[1].getPosition(i), moons[1].getVelocity(i),
                    moons[2].getPosition(i), moons[2].getVelocity(i),
                    moons[3].getPosition(i), moons[3].getVelocity(i)
            );

            axisStatesSorted.get(i).add(axisState);
            axisStatesUnsorted.get(i).add(axisState);
        }

        Integer[] cycleStarts = {null, null, null};
        Integer[] cycleLengths = {null, null, null};

        while (Arrays.asList(cycleStarts).contains(null)) {
            simulateTick(moons);

            for (int i = 0; i < 3; i++) {
                if (cycleStarts[i] != null) {
                    continue;
                }

                AxisState axisState = new AxisState(
                        moons[0].getPosition(i), moons[0].getVelocity(i),
                        moons[1].getPosition(i), moons[1].getVelocity(i),
                        moons[2].getPosition(i), moons[2].getVelocity(i),
                        moons[3].getPosition(i), moons[3].getVelocity(i)
                );

                if (axisStatesUnsorted.get(i).contains(axisState)) {
                    cycleStarts[i] = axisStatesSorted.get(i).indexOf(axisState);
                    cycleLengths[i] = axisStatesSorted.get(i).size() - cycleStarts[i]; //Off by 1 error?
                } else {
                    axisStatesSorted.get(i).add(axisState);
                    axisStatesUnsorted.get(i).add(axisState);
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            if (cycleLengths[i] == null || cycleStarts[i] == null) {
                throw new RuntimeException("Null in cycleLengths or cycleStarts.");
            }
        }

        for (long multiplier0 = 1; multiplier0 < Integer.MAX_VALUE; multiplier0++) {
            long steps0 = multiplier0 * cycleLengths[0] + cycleStarts[0];

            double multiplier1 = ((double) steps0 - cycleStarts[1]) / cycleLengths[1];
            double multiplier2 = ((double) steps0 - cycleStarts[2]) / cycleLengths[2];

            if (multiplier1 == Math.floor(multiplier1) && multiplier2 == Math.floor(multiplier2)) {
                return String.format("%d", steps0);
            }
        }

        throw new RuntimeException("Max iterations reached.");
    }

    private void simulateTick(Moon[] moons) {
        for (Moon moon : moons) {
            for (Moon other : moons) {
                if (moon == other) {
                    continue;
                }

                int[] gravity = new int[3];

                for (int i = 0; i < 3; i++) {
                    if (moon.getPosition(i) < other.getPosition(i)) {
                        gravity[i] = 1;
                    } else if (moon.getPosition(i) > other.getPosition(i)) {
                        gravity[i] = -1;
                    }
                }

                moon.addToVelocity(gravity);
            }
        }

        for (Moon moon : moons) {
            moon.move();
        }
    }
}
