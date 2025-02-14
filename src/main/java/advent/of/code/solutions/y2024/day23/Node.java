package advent.of.code.solutions.y2024.day23;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Node implements Comparable<Node> {
    @Getter
    private final String Name;
    private final Set<Node> Neighbors = new HashSet<>();

    public Node(String name) {
        Name = name;
    }

    public void addNeighbor(Node neighbor) {
        Neighbors.add(neighbor);
    }

    @Override
    public int compareTo(Node other) {
        return this.Name.compareTo(other.Name);
    }

    public boolean isNeighbor(Node other) {
        return this.Neighbors.contains(other);
    }

    public Set<Node> getNeighbors() {
        return Collections.unmodifiableSet(Neighbors);
    }
}
