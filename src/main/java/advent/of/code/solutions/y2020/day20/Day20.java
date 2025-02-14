package advent.of.code.solutions.y2020.day20;

import advent.of.code.solutions.Day;
import advent.of.code.utils.MatrixUtils;
import advent.of.code.utils.Directions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Too many indentations, but I made nice use of ArrayUtils and Directions, so I'm quite satisfied
 */
@SuppressWarnings("unused")
public class Day20 implements Day {
    private static final int TILE_IMAGE_SIDE_LENGTH = 8;
    private static final int BIG_IMAGE_SIDE_LENGTH = 96;
    private static final int TILES_PER_SIDE = 12;

    private static final String SEA_MONSTER_STRING = "..................#.\n#....##....##....###\n.#..#..#..#..#..#...";
    private static final Boolean[][] SEA_MONSTER = Arrays.stream(SEA_MONSTER_STRING.split("\n"))
            .map(l -> l.chars().mapToObj(ch -> (char) ch == '#').toArray(Boolean[]::new))
            .toArray(Boolean[][]::new);

    @Override
    public String part1(String inputString) {
        Map<Integer, Set<Tile>> input = parseInputString(inputString);
        int firstId = Integer.parseInt(inputString.split("\n")[0].substring(5, 9));

        if (input.get(firstId).size() != 1) {
            throw new RuntimeException("Bad parsing.");
        }
        Tile firstTile = input.get(firstId).stream().findAny().orElseThrow();

        Set<Tile> matchedTiles = matchTiles(input, firstTile);

        Set<Integer> cornerIds = matchedTiles.stream().filter(Tile::isCorner).map(Tile::getId).collect(Collectors.toSet());

        long result = 1;
        for (int cornerId : cornerIds) {
            result *= cornerId;
        }

        return String.format("%d", result);
    }

    @Override
    public String part2(String inputString) {
        Map<Integer, Set<Tile>> input = parseInputString(inputString);
        int firstId = Integer.parseInt(inputString.split("\n")[0].substring(5, 9));

        if (input.get(firstId).size() != 1) {
            throw new RuntimeException("Bad parsing.");
        }
        Tile firstTile = input.get(firstId).stream().findAny().orElseThrow();
        Set<Tile> matchedTiles = matchTiles(input, firstTile);
        Tile topLeftCorner = matchedTiles.stream().filter(Tile::isTopLeftCorner).toList().getFirst();

        Boolean[][] finalImage = new Boolean[BIG_IMAGE_SIDE_LENGTH][BIG_IMAGE_SIDE_LENGTH];

        for (int row = 0; row < TILES_PER_SIDE; row++) {
            for (int column = 0; column < TILES_PER_SIDE; column++) {
                for (int tileRow = 0; tileRow < TILE_IMAGE_SIDE_LENGTH; tileRow++) {
                    for (int tileColumn = 0; tileColumn < TILE_IMAGE_SIDE_LENGTH; tileColumn++) {
                        finalImage[row * TILE_IMAGE_SIDE_LENGTH + tileRow][column * TILE_IMAGE_SIDE_LENGTH + tileColumn] =
                                topLeftCorner.get(row, column).getBit(tileRow, tileColumn);
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (removeSeaMonsters(finalImage)) {
                break;
            }

            finalImage = MatrixUtils.rotateClockwise(finalImage);

            if (i == 4) {
                finalImage = MatrixUtils.flipVertically(finalImage);
            }
        }

        return String.format("%d", MatrixUtils.count(finalImage, true));
    }

    private Map<Integer, Set<Tile>> parseInputString(String inputString) {
        Map<Integer, Set<Tile>> returnValue = new HashMap<>();
        boolean isFirst = true;

        for (String tileString : inputString.split("\n\n")) {
            Set<Tile> tileSet = new HashSet<>();

            Tile mainTile = new Tile(tileString);
            tileSet.add(mainTile);
            if (!isFirst) {
                tileSet.addAll(mainTile.getAllVersions());
            } else {
                isFirst = false;
            }
            returnValue.put(mainTile.getId(), Collections.unmodifiableSet(tileSet));
        }

        return Collections.unmodifiableMap(returnValue);
    }

    private Set<Tile> matchTiles(Map<Integer, Set<Tile>> input, Tile firstTile) {
        for (int currentId : input.keySet()) {
            for (Tile currentTile : input.get(currentId)) {
                for (int otherId : input.keySet()) {
                    if (currentId == otherId) {
                        continue;
                    }

                    for (Tile otherTile : input.get(otherId)) {
                        for (Directions direction : Directions.values()) {
                            if (currentTile.matches(direction, otherTile)) {
                                currentTile.addMatch(direction, otherTile);
                            }
                        }
                    }
                }
            }
        }

        Set<Tile> matchedTiles = new HashSet<>();
        matchedTiles.add(firstTile);
        matchedTiles.addAll(firstTile.getMatchedTiles());

        int oldSize = matchedTiles.size();

        while (true) {
            matchedTiles = matchedTiles.stream().flatMap(t -> t.getMatchedTiles().stream()).collect(Collectors.toSet());
            if (matchedTiles.size() == oldSize) {
                break;
            }

            oldSize = matchedTiles.size();
        }

        if (matchedTiles.stream().map(Tile::getId).collect(Collectors.toSet()).size() != input.size()) {
            throw new RuntimeException("Bat matching.");
        }

        return matchedTiles;
    }

    private boolean removeSeaMonsters(Boolean[][] image) {
        boolean removedSeaMonster = false;

        for (int row = 0; row < image.length - SEA_MONSTER.length; row++) {
            for (int column = 0; column < image[row].length - SEA_MONSTER[0].length; column++) {
                boolean isSeaMonster = true;
                for (int monsterRow = 0; monsterRow < SEA_MONSTER.length && isSeaMonster; monsterRow++) {
                    for (int monsterColumn = 0; monsterColumn < SEA_MONSTER[monsterRow].length; monsterColumn++) {
                        if (SEA_MONSTER[monsterRow][monsterColumn] && !image[row + monsterRow][column + monsterColumn]) {
                            isSeaMonster = false;
                            break;
                        }
                    }
                }

                if (isSeaMonster) {
                    removedSeaMonster = true;
                    removeSeaMonster(image, row, column);
                }
            }
        }
        return removedSeaMonster;
    }

    private void removeSeaMonster(Boolean[][] image, int row, int column) {
        for (int monsterRow = 0; monsterRow < SEA_MONSTER.length; monsterRow++) {
            for (int monsterColumn = 0; monsterColumn < SEA_MONSTER[monsterRow].length; monsterColumn++) {
                if (SEA_MONSTER[monsterRow][monsterColumn]) {
                    image[row + monsterRow][column + monsterColumn] = false;
                }
            }
        }
    }
}
