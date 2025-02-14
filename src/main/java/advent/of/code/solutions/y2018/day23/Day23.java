package advent.of.code.solutions.y2018.day23;

import advent.of.code.solutions.Day;
import advent.of.code.utils.SetUtils;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day23 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Nanobot> input = parseInputString(inputString);

        Nanobot maxRadiusBot = input.stream().findAny().orElseThrow();
        for (Nanobot nanobot : input) {
            if (nanobot.getRadius() > maxRadiusBot.getRadius()) {
                maxRadiusBot = nanobot;
            }
        }

        return String.format("%d", maxRadiusBot.getInRange().size() + 1);
    }

    /**
     * I was stumped, so I searched for small hints online. One of the results was
     * <a href="https://todd.ginsberg.com/post/advent-of-code/2018/day23/">https://todd.ginsberg.com/post/advent-of-code/2018/day23/</a>
     * which had in the search engine snippet "Today we'll learn about finding cliques in a graph".
     * That made it click for me.
     * <p>
     * I gave up overall on this puzzle and copied the minDistance calculation as well. I don't fully get it.
     * On the other hand I don't think I would have ever come up with it myself. I was trying to find a
     * way to model the volume in common between two nanobots, wanting to refine that volume over
     * the whole clique. That proved too complicated.
     * <p>
     * I don't like using a solution I don't really understand, but I wanted to be done with this.
     * It was more frustrating than y2019d22.
     */
    @Override
    public String part2(String inputString) {
        Set<Nanobot> input = parseInputString(inputString);
        Set<Nanobot> maximalClique = findMaximalClique(input);

        return String.format("%d", maximalClique.stream()
                .mapToLong(n -> n.distanceFromOrigin() - n.getRadius())
                .max()
                .orElseThrow());
    }

    private Set<Nanobot> parseInputString(String inputString) {
        Set<Nanobot> input = Arrays.stream(inputString.split("\n"))
                .map(Nanobot::new)
                .collect(Collectors.toSet());

        for (Nanobot nanobot : input) {
            for (Nanobot other : input) {
                if (nanobot == other) {
                    continue;
                }

                if (nanobot.hasInRange(other)) {
                    nanobot.addInRange(other);
                }

                if (nanobot.hasPointsInCommon(other)) {
                    nanobot.addWithPointInCommon(other);
                    other.addWithPointInCommon(nanobot);
                }
            }
        }

        return input;
    }

    /**
     * I tried implementing the same solution I used for y2024d23, but it took too long.
     * I even tried with pivot, but that did not work either.
     * This implementation was adapted from
     * <a href="https://todd.ginsberg.com/post/advent-of-code/2018/day23/">https://todd.ginsberg.com/post/advent-of-code/2018/day23/</a>
     * It looks like it chooses the pivot with the most neighbors, which my implementation did not
     * do, picking a random one instead. On the other hand it pushed me to make SetUtils, which
     * is not bad. Still feels like a cheat.
     */
    private Set<Nanobot> findMaximalClique(Set<Nanobot> nanobots) {
        Map<Nanobot, Set<Nanobot>> adjacencyMap = new HashMap<>();
        for (Nanobot nanobot : nanobots) {
            adjacencyMap.put(nanobot, nanobot.getWithPointInCommon());
        }
        return bronKerbosch(adjacencyMap, adjacencyMap.keySet(), new HashSet<>(), new HashSet<>());
    }

    private Set<Nanobot> bronKerbosch(Map<Nanobot, Set<Nanobot>> adjacencyMap, Set<Nanobot> P, Set<Nanobot> R, Set<Nanobot> X) {
        Set<Nanobot> maxClique = new HashSet<>();

        if (P.isEmpty() && X.isEmpty()) {
            return R;
        } else {
            Nanobot mostNeighborsOfPAndX = SetUtils.union(P, X)
                    .stream()
                    .max(Comparator.comparingInt(l -> l.getWithPointInCommon().size()))
                    .orElseThrow();

            Set<Nanobot> pWithoutNeighbors = SetUtils.difference(P, adjacencyMap.get(mostNeighborsOfPAndX));

            for (Nanobot nanobot : pWithoutNeighbors) {
                Set<Nanobot> neighbors = adjacencyMap.get(nanobot);
                Set<Nanobot> tempClique = bronKerbosch(
                        adjacencyMap,
                        SetUtils.intersection(P, neighbors),
                        SetUtils.add(R, nanobot),
                        SetUtils.intersection(X, neighbors)
                );

                if (tempClique.size() > maxClique.size()) {
                    maxClique = tempClique;
                }
            }
        }

        return maxClique;
    }
}
