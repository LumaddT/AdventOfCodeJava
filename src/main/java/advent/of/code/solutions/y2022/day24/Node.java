package advent.of.code.solutions.y2022.day24;

import advent.of.code.utils.coordinates.Coordinate;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Node {
    @Getter
    private final Coordinate Coordinate;

    private final Set<Node> Next = new HashSet<>();

    public Node(advent.of.code.utils.coordinates.Coordinate coordinate) {
        Coordinate = coordinate;
    }

    public void addNext(Node node) {
        Next.add(node);
    }

    public Set<Node> getNext() {
        return Collections.unmodifiableSet(Next);
    }
}
