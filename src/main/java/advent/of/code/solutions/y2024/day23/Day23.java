package advent.of.code.solutions.y2024.day23;

import advent.of.code.solutions.Day;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day23 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Node> input = parseInputString(inputString);
        Set<ThreeClique> threeCliques = findThreeCliques(input);

        return String.format("%d", threeCliques.stream()
                .filter(c -> c.nodeStartsWith('t'))
                .count());
    }

    @Override
    public String part2(String inputString) {
        Set<Node> input = parseInputString(inputString);
        Set<Node> maximalClique = findMaximalClique(input);

        return maximalClique.stream()
                .map(Node::getName)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private Set<Node> parseInputString(String inputString) {
        Map<String, Node> returnValue = new HashMap<>();

        String[][] inputSplit = Arrays.stream(inputString.split("\n"))
                .map(l -> l.split("-"))
                .toArray(String[][]::new);

        for (String[] line : inputSplit) {
            returnValue.putIfAbsent(line[0], new Node(line[0]));
            returnValue.putIfAbsent(line[1], new Node(line[1]));
        }

        for (String[] line : inputSplit) {
            Node left = returnValue.get(line[0]);
            Node right = returnValue.get(line[1]);

            left.addNeighbor(right);
            right.addNeighbor(left);
        }

        return Set.copyOf(returnValue.values());
    }

    private Set<ThreeClique> findThreeCliques(Set<Node> nodes) {
        Set<ThreeClique> returnValue = new HashSet<>();
        for (Node firstNode : nodes) {
            for (Node secondNode : firstNode.getNeighbors()) {
                for (Node thirdNode : firstNode.getNeighbors()) {
                    if (secondNode.isNeighbor(thirdNode)) {
                        returnValue.add(new ThreeClique(firstNode, secondNode, thirdNode));
                    }
                }
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    // Bron–Kerbosch algorithm
    // https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm#Without_pivoting
    private Set<Node> findMaximalClique(Set<Node> nodes) {
        Set<Node> P = new HashSet<>(nodes);
        Set<Node> R = new HashSet<>();
        Set<Node> X = new HashSet<>();

        return bronKerbosch(P, R, X);
    }

    private Set<Node> bronKerbosch(Set<Node> P, Set<Node> R, Set<Node> X) {
        if (P.isEmpty() && X.isEmpty()) {
            return R;
        }

        Set<Node> currentMax = new HashSet<>();
        Set<Node> PCopy = new HashSet<>(P);

        for (Node node : P) {
            Set<Node> newP = new HashSet<>(PCopy);
            newP.retainAll(node.getNeighbors());
            Set<Node> newR = new HashSet<>(R);
            newR.add(node);
            Set<Node> newX = new HashSet<>(X);
            newX.retainAll(node.getNeighbors());

            Set<Node> candidate = bronKerbosch(newP, newR, newX);

            if (candidate.size() > currentMax.size()) {
                currentMax = candidate;
            }


            PCopy.remove(node);
            X.add(node);
        }

        return currentMax;
    }
}
