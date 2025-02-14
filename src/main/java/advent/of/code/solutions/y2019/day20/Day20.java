package advent.of.code.solutions.y2019.day20;

import advent.of.code.solutions.Day;

//TODO: This is one of the worst, most spaghetti code solutions I have written for AoC
// It works but it's very ugly. I should give it another go eventually. One of the things
// to notice is that in Maze.ValidDestinations the vast majority of Lists has size() = 1,
// so many can be coalesced into a single entity that keeps track of entrance, final exit, distance
// and layer variation.

// This code was left mostly untouched during the refactor, only opting to use the public
// shared Coordinate class rather than a custom one, with the comparison method moved to Maze.compareCoordinates().
@SuppressWarnings("unused")
public class Day20 implements Day {
    @Override
    public String part1(String inputString) {
        Maze maze = new Maze(inputString);

        return String.format("%d", maze.getDistanceEntranceToExitPart1());
    }

    @Override
    public String part2(String inputString) {
        Maze maze = new Maze(inputString);

        return String.format("%d", maze.getDistanceEntranceToExitPart2());
    }
}
