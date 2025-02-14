package advent.of.code.solutions.y2019.day06;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class CelestialObject {
    @Getter
    private final String Name;
    @Setter
    private CelestialObject Orbits;
    private Set<CelestialObject> DirectAndIndirectOrbits = null;

    public CelestialObject(String name) {
        Name = name;
    }

    public Set<CelestialObject> getDirectAndIndirectOrbits() {
        if (DirectAndIndirectOrbits != null) {
            return DirectAndIndirectOrbits;
        }

        if (Orbits == null) {
            return new HashSet<>();
        }

        Set<CelestialObject> returnValue = new HashSet<>(Orbits.getDirectAndIndirectOrbits());
        returnValue.add(Orbits);

        DirectAndIndirectOrbits = Collections.unmodifiableSet(returnValue);
        return DirectAndIndirectOrbits;
    }
}
