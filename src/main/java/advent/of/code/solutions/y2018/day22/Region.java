package advent.of.code.solutions.y2018.day22;

import advent.of.code.utils.coordinates.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Region implements Comparable<Region> {
    private static final Map<RegionType, Set<Gear>> ALLOWED_GEAR = Map.of(
            RegionType.ROCKY, Set.of(Gear.TORCH, Gear.CLIMBING_GEAR),
            RegionType.WET, Set.of(Gear.CLIMBING_GEAR, Gear.NEITHER),
            RegionType.NARROW, Set.of(Gear.TORCH, Gear.NEITHER));
    private static final int TIME_TO_SWAP = 7;

    private final Coordinate Coord;

    private final int ErosionLevel;
    private final RegionType Type;
    private final Map<Gear, Integer> TimeToReach = new HashMap<>();

    private final Region Up;
    private final Region Left;
    private Region Down = null;
    private Region Right = null;

    private final Coordinate TargetCoordinate;
    private final int Depth;

    public Region(Coordinate targetCoordinate, int depth) {
        this(new Coordinate(0, 0), null, null, targetCoordinate, depth);
        setTime(0, Gear.TORCH);
    }

    public Region(Coordinate coordinate, Region up, Region left, Coordinate targetCoordinate, int depth) {
        Coord = coordinate;

        Up = up;
        if (Up != null) {
            Up.Down = this;
        }

        Left = left;
        if (Left != null) {
            Left.Right = this;
        }

        TargetCoordinate = targetCoordinate;
        Depth = depth;

        ErosionLevel = (calculateGeologicIndex() + depth) % 20183;
        Type = calculateRegionType();
        for (Gear gear : ALLOWED_GEAR.get(Type)) {
            TimeToReach.put(gear, Integer.MAX_VALUE);
        }
    }

    private void setTime(int newTime, Gear gear) {
        if (!TimeToReach.containsKey(gear)) {
            throw new RuntimeException("Bad gear in time setter");
        }

        if (newTime >= TimeToReach.get(gear)) {
            throw new RuntimeException("New time is greater than old time");
        }

        TimeToReach.put(gear, newTime);

        newTime += TIME_TO_SWAP;
        Set<Gear> keySet = new HashSet<>(TimeToReach.keySet());
        keySet.remove(gear);
        Gear otherGear = keySet.stream().findAny().orElseThrow();

        if (newTime < TimeToReach.get(otherGear)) {
            TimeToReach.put(otherGear, newTime);
        }
    }

    public int getTime(Gear gear) {
        if (!TimeToReach.containsKey(gear)) {
            throw new RuntimeException("Bad gear in time getter");
        }

        return TimeToReach.get(gear);
    }

    // Far from perfect but it worked for me
    private RegionType calculateRegionType() {
        int regionTypeNumber = ErosionLevel % 3;

        return switch (regionTypeNumber) {
            case 0 -> RegionType.ROCKY;
            case 1 -> RegionType.WET;
            case 2 -> RegionType.NARROW;
            default -> throw new RuntimeException("Impossible state exception.");
        };
    }

    private int calculateGeologicIndex() {
        if (Coord.equals(Coordinate.ORIGIN) || Coord.equals(TargetCoordinate)) {
            return 0;
        }

        if (Coord.Row() == 0) {
            return Coord.Column() * 16807;
        }

        if (Coord.Column() == 0) {
            return Coord.Row() * 48271;
        }

        return Up.ErosionLevel * Left.ErosionLevel;
    }

    public Region getDown() {
        if (Down != null) {
            return Down;
        }

        if (Coord.Column() == 0) {
            return new Region(Coord.down(), this, null, TargetCoordinate, Depth);
        }

        return new Region(Coord.down(), this, this.Left.getDown(), TargetCoordinate, Depth);
    }

    public Region getRight() {
        if (Right != null) {
            return Right;
        }

        if (Coord.Row() == 0) {
            return new Region(Coord.right(), null, this, TargetCoordinate, Depth);
        }

        return new Region(Coord.right(), this.Up.getRight(), this, TargetCoordinate, Depth);
    }

    public int getRiskLevel() {
        return switch (Type) {
            case ROCKY -> 0;
            case WET -> 1;
            case NARROW -> 2;
        };
    }

    public Set<Region> getNeighbors() {
        if (Coord.Row() == 0 && Coord.Column() == 0) {
            return Set.of(getDown(), getRight());
        }

        if (Coord.Row() == 0) {
            return Set.of(getDown(), getRight(), Left);
        }

        if (Coord.Column() == 0) {
            return Set.of(getDown(), getRight(), Up);
        }

        return Set.of(getDown(), getRight(), Up, Left);
    }

    public boolean updateTimes(Region destination) {
        if (destination == null) {
            return false;
        }

        boolean updated = false;

        if (this.Type == destination.Type) {
            for (Gear gear : ALLOWED_GEAR.get(this.Type)) {
                if (this.getTime(gear) + 1 < destination.getTime(gear)) {
                    destination.setTime(this.getTime(gear) + 1, gear);
                    updated = true;
                }
            }
        } else {
            Set<Gear> gearsSource = new HashSet<>(ALLOWED_GEAR.get(this.Type));
            gearsSource.retainAll(ALLOWED_GEAR.get(destination.Type));
            if (gearsSource.size() != 1) {
                throw new RuntimeException("Bad common gear");
            }
            Gear gearInCommon = gearsSource.stream().findAny().orElseThrow();
            if (this.getTime(gearInCommon) + 1 < destination.getTime(gearInCommon)) {
                destination.setTime(this.getTime(gearInCommon) + 1, gearInCommon);
                updated = true;
            }
        }

        return updated;
    }

    public boolean isTarget() {
        return Coord.equals(TargetCoordinate);
    }

    @Override
    public int compareTo(Region other) {
        int thisTime = TimeToReach.values().stream().min(Integer::compare).orElseThrow();
        int otherTime = other.TimeToReach.values().stream().min(Integer::compare).orElseThrow();

        return thisTime - otherTime;
    }
}
