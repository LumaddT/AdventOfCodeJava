package advent.of.code.solutions.y2022.day16;

import lombok.Getter;

import java.util.*;

class Node {
    @Getter
    private final String Name;
    @Getter
    private final int FlowRate;

    private final Set<Node> Neighbors = new HashSet<>();
    private final Map<Node, Integer> EffectiveDistances = new HashMap<>();

    public Node(String name, int flowRate) {
        Name = name;
        FlowRate = flowRate;
    }

    public void addNeighbor(Node node) {
        Neighbors.add(node);
    }

    public void addEffectiveDistance(Node node, int distance) {
        if (EffectiveDistances.containsKey(node)) {
            throw new RuntimeException("Bad setting of effective distance.");
        }

        EffectiveDistances.put(node, distance);
    }

    public Set<Node> getNeighbors() {
        return Collections.unmodifiableSet(Neighbors);
    }

    public Map<Node, Integer> getEffectiveDistances() {
        return Collections.unmodifiableMap(EffectiveDistances);
    }

    public int getPotentialTotalFlow(int timeLeft) {
        if (timeLeft <= 0) {
            return 0;
        }
        return FlowRate * timeLeft;
    }
}
