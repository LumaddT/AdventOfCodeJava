package advent.of.code.solutions.y2020.day11;

import advent.of.code.solutions.Day;
import advent.of.code.utils.*;
import advent.of.code.utils.coordinates.Coordinate;

import java.util.*;


@SuppressWarnings("unused")
public class Day11 implements Day {
    @Override
    public String part1(String inputString) {
        Set<Coordinate> seats = parseInputString(inputString);
        Set<Coordinate> occupiedSeats = new HashSet<>();

        while (true) {
            Set<Coordinate> oldOccupiedSeats = occupiedSeats;

            occupiedSeats = performRoundPart1(seats, occupiedSeats);

            if (oldOccupiedSeats.equals(occupiedSeats)) {
                return String.format("%d", occupiedSeats.size());
            }
        }
    }

    @Override
    public String part2(String inputString) {
        Set<Coordinate> seats = parseInputString(inputString);
        Set<Coordinate> occupiedSeats = new HashSet<>();
        Map<Coordinate, Set<Coordinate>> visibleSeats = getVisibleSeatsMap(seats);

        while (true) {
            Set<Coordinate> oldOccupiedSeats = occupiedSeats;

            occupiedSeats = performRoundPart2(seats, occupiedSeats, visibleSeats);

            if (oldOccupiedSeats.equals(occupiedSeats)) {
                return String.format("%d", occupiedSeats.size());
            }
        }
    }

    private Set<Coordinate> parseInputString(String inputString) {
        Set<Coordinate> seats = new HashSet<>();
        char[][] grid = ParsingUtils.toGrid(inputString);

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == 'L') {
                    seats.add(new Coordinate(row, column));
                }
            }
        }

        return Collections.unmodifiableSet(seats);
    }

    private Set<Coordinate> performRoundPart1(Set<Coordinate> seats, Set<Coordinate> occupiedSeats) {
        Set<Coordinate> returnValue = new HashSet<>(occupiedSeats);

        for (Coordinate seat : seats) {
            int adjacentSeatsOccupied = SetUtils.intersection(seat.adjacent(), occupiedSeats).size();

            if (adjacentSeatsOccupied == 0) {
                returnValue.add(seat);
            } else if (adjacentSeatsOccupied >= 4) {
                returnValue.remove(seat);
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private Set<Coordinate> performRoundPart2(Set<Coordinate> seats, Set<Coordinate> occupiedSeats, Map<Coordinate, Set<Coordinate>> visibleSeats) {
        Set<Coordinate> returnValue = new HashSet<>(occupiedSeats);

        for (Coordinate seat : seats) {
            int visibleSeatsOccupied = SetUtils.intersection(visibleSeats.get(seat), occupiedSeats).size();

            if (visibleSeatsOccupied == 0) {
                returnValue.add(seat);
            } else if (visibleSeatsOccupied >= 5) {
                returnValue.remove(seat);
            }
        }

        return Collections.unmodifiableSet(returnValue);
    }

    private Map<Coordinate, Set<Coordinate>> getVisibleSeatsMap(Set<Coordinate> seats) {
        int maxRow = seats.stream().mapToInt(Coordinate::Row).max().orElseThrow();
        int maxColumn = seats.stream().mapToInt(Coordinate::Column).max().orElseThrow();

        Map<Coordinate, Set<Coordinate>> returnValue = new HashMap<>();

        for (Coordinate coordinate : seats) {
            returnValue.put(coordinate, new HashSet<>());

            for (Directions direction : Directions.values()) {
                Coordinate visibleCoordinate = coordinate.move(direction);

                while (visibleCoordinate.isInRange(0, 0, maxRow, maxColumn)) {
                    if (seats.contains(visibleCoordinate)) {
                        returnValue.get(coordinate).add(visibleCoordinate);
                        break;
                    }

                    visibleCoordinate = visibleCoordinate.move(direction);
                }
            }

            for (Directions vertical : DirectionUtils.VERTICAL_DIRECTIONS) {
                for (Directions horizontal : DirectionUtils.HORIZONTAL_DIRECTIONS) {
                    Coordinate seenCoordinate = coordinate.move(vertical).move(horizontal);

                    while (seenCoordinate.isInRange(0, 0, maxRow, maxColumn)) {
                        if (seats.contains(seenCoordinate)) {
                            returnValue.get(coordinate).add(seenCoordinate);
                            break;
                        }

                        seenCoordinate = seenCoordinate.move(vertical).move(horizontal);
                    }
                }
            }
        }

        return Collections.unmodifiableMap(returnValue);
    }
}
