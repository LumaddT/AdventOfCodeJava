package advent.of.code.solutions.y2018.day23;

import advent.of.code.utils.RegexUtils;
import advent.of.code.utils.coordinates.Coordinate3;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

class Nanobot {
    private final Coordinate3 Location;
    @Getter
    private final long Radius;
    private final Set<Nanobot> InRange = new HashSet<>();
    private final Set<Nanobot> WithPointInCommon = new HashSet<>();

    public Nanobot(String inputString) {
        Matcher matcher = RegexUtils.match("^pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)$", inputString, 4);
        long x = Long.parseLong(matcher.group(1));
        long y = Long.parseLong(matcher.group(2));
        long z = Long.parseLong(matcher.group(3));
        long radius = Long.parseLong(matcher.group(4));

        Location = new Coordinate3(x, y, z);
        Radius = radius;
    }

    public boolean hasInRange(Nanobot other) {
        return hasInRange(other.Location);
    }

    // Copied withinRangeOfSharedPoint() from
    // https://todd.ginsberg.com/post/advent-of-code/2018/day23/
    public boolean hasPointsInCommon(Nanobot other) {
        return this.Location.distance(other.Location) <= this.Radius + other.Radius;
    }

    public boolean hasInRange(Coordinate3 point) {
        return this.Location.distance(point) <= Radius;
    }

    public void addInRange(Nanobot nanobot) {
        InRange.add(nanobot);
    }

    public void addWithPointInCommon(Nanobot nanobot) {
        WithPointInCommon.add(nanobot);
    }

    public Set<Nanobot> getInRange() {
        return Collections.unmodifiableSet(InRange);
    }

    public Set<Nanobot> getWithPointInCommon() {
        return Collections.unmodifiableSet(WithPointInCommon);
    }

    public long distanceFromOrigin() {
        return Location.distance(Coordinate3.ORIGIN);
    }
}