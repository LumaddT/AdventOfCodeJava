package advent.of.code.solutions.y2021.day11;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

class Octopus {
    private int EnergyLevel;

    private final Set<Octopus> Neighbors = new HashSet<>();

    @Getter
    private boolean Flashed = false;

    public Octopus(int energyLevel) {
        EnergyLevel = energyLevel;
    }

    public void addNeighbor(Octopus neighbor) {
        Neighbors.add(neighbor);
    }

    public void increaseEnergy() {
        EnergyLevel++;

        if (EnergyLevel > 9 && !Flashed) {
            Flashed = true;
            Neighbors.forEach(Octopus::increaseEnergy);
        }
    }

    public void resetIfFlashed() {
        if (Flashed) {
            EnergyLevel = 0;
            Flashed = false;
        }
    }
}
