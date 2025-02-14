package advent.of.code.solutions.y2021.day12;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Cave {
    @Getter
    private final String Name;

    private final Set<Cave> Neighbors = new HashSet<>();
    @Getter
    private final boolean Small;

    public Cave(String name) {
        Name = name;
        Small = Name.toLowerCase().equals(Name);
    }

    public void addNeighbor(Cave neighbor) {
        Neighbors.add(neighbor);
        neighbor.Neighbors.add(this);
    }

    public Set<Cave> getNeighbors() {
        return Collections.unmodifiableSet(Neighbors);
    }
}
