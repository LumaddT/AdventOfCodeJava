package advent.of.code.solutions.y2022.day24;

import advent.of.code.solutions.Day;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day24 implements Day {
    @Override
    public String part1(String inputString) {
        Valley valley = new Valley(inputString);
        Node initialNode = generateGraph(valley);

        Set<Node> currentNodes = new HashSet<>();
        currentNodes.add(initialNode);

        for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
            currentNodes = currentNodes.stream()
                    .flatMap(n -> n.getNext().stream())
                    .collect(Collectors.toSet());

            if (currentNodes.stream().map(Node::getCoordinate).collect(Collectors.toSet()).contains(valley.getExit())) {
                return String.format("%d", i + 1);
            }
        }

        throw new RuntimeException("Bad solution.");
    }

    @Override
    public String part2(String inputString) {
        Valley valley = new Valley(inputString);
        Node initialNode = generateGraph(valley);

        Set<Node> currentNodes = new HashSet<>();
        currentNodes.add(initialNode);

        int trip = 0;

        for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
            currentNodes = currentNodes.stream()
                    .flatMap(n -> n.getNext().stream())
                    .collect(Collectors.toSet());

            if (trip == 0 && currentNodes.stream().map(Node::getCoordinate).collect(Collectors.toSet()).contains(valley.getExit())) {
                currentNodes = currentNodes.stream().filter(n -> n.getCoordinate().equals(valley.getExit())).collect(Collectors.toSet());
                trip++;
            } else if (trip == 1 && currentNodes.stream().map(Node::getCoordinate).collect(Collectors.toSet()).contains(valley.getEntrance())) {
                currentNodes = currentNodes.stream().filter(n -> n.getCoordinate().equals(valley.getEntrance())).collect(Collectors.toSet());
                trip++;
            } else if (trip == 2 && currentNodes.stream().map(Node::getCoordinate).collect(Collectors.toSet()).contains(valley.getExit())) {
                return String.format("%d", i + 1);
            }
        }

        throw new RuntimeException("Bad solution.");
    }

    private Node generateGraph(Valley valley) {
        List<Map<Coordinate, Node>> allFormations = new ArrayList<>();
        for (int i = 0; i < valley.getRepeatCycles(); i++) {
            allFormations.add(valley.getFreeSpaces()
                    .stream()
                    .collect(Collectors.toMap(
                            c -> c,
                            Node::new
                    )));
            valley.move();
        }

        for (int i = 0; i < valley.getRepeatCycles(); i++) {
            for (Node node : allFormations.get(i).values()) {
                for (Coordinate coordinate : node.getCoordinate().orthogonallyAdjacent()) {
                    Node next = allFormations.get((i + 1) % allFormations.size()).get(coordinate);
                    if (next != null) {
                        node.addNext(next);
                    }
                }

                Node next = allFormations.get((i + 1) % allFormations.size()).get(node.getCoordinate());
                if (next != null) {
                    node.addNext(next);
                }
            }
        }

        return allFormations.getFirst().get(valley.getEntrance());
    }
}
