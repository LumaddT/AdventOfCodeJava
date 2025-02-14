package advent.of.code.solutions.y2024.day05;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class Page {
    @Getter
    private final int Number;
    private final Set<Page> After = new HashSet<>();

    public Page(int number) {
        Number = number;
    }

    public void addAfter(Page page) {
        After.add(page);
    }

    public Set<Page> getAfter() {
        return Collections.unmodifiableSet(After);
    }
}

