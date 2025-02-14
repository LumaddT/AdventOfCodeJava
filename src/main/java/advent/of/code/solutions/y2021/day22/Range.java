package advent.of.code.solutions.y2021.day22;

import java.util.ArrayList;
import java.util.List;

record Range(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
    public long size() {
        return (long) (Math.abs(maxX - minX) + 1) * (Math.abs(maxY - minY) + 1) * (Math.abs(maxZ - minZ) + 1);
    }

    public List<Range> remove(Range other) {
        List<Range> newRanges = getRawNewRanges(other);

        boolean modified;
        do {
            List<Range> coalescedRanges = new ArrayList<>(newRanges);
            modified = false;
            for (int i = 0; i < newRanges.size() - 1; i++) {
                for (int j = i + 1; j < newRanges.size(); j++) {
                    Range first = newRanges.get(i);
                    Range second = newRanges.get(j);

                    Range coalescedRange = first.coalesce(second);

                    if (coalescedRange != null) {
                        coalescedRanges.add(coalescedRange);
                        coalescedRanges.remove(first);
                        coalescedRanges.remove(second);

                        modified = true;
                        break;
                    }
                }
                if (modified) {
                    break;
                }
            }

            newRanges = coalescedRanges;
        } while (modified);

        return newRanges;
    }

    private List<Range> getRawNewRanges(Range other) {
        List<Range> newRanges = new ArrayList<>();

        for (int xType = 0; xType < 3; xType++) {
            for (int yType = 0; yType < 3; yType++) {
                for (int zType = 0; zType < 3; zType++) {
                    if (xType == 0 && yType == 0 && zType == 0) {
                        continue;
                    }

                    int newMinX;
                    int newMaxX;
                    int newMinY;
                    int newMaxY;
                    int newMinZ;
                    int newMaxZ;

                    switch (xType) {
                        case 0 -> {
                            newMinX = Math.max(minX, other.minX);
                            newMaxX = Math.min(maxX, other.maxX);
                        }
                        case 1 -> {
                            newMinX = Math.max(minX, other.maxX + 1);
                            newMaxX = maxX;
                        }
                        case 2 -> {
                            newMinX = minX;
                            newMaxX = Math.min(maxX, other.minX - 1);
                        }
                        default -> throw new RuntimeException("Illegal xType");
                    }

                    switch (yType) {
                        case 0 -> {
                            newMinY = Math.max(minY, other.minY);
                            newMaxY = Math.min(maxY, other.maxY);
                        }
                        case 1 -> {
                            newMinY = Math.max(minY, other.maxY + 1);
                            newMaxY = maxY;
                        }
                        case 2 -> {
                            newMinY = minY;
                            newMaxY = Math.min(maxY, other.minY - 1);
                        }
                        default -> throw new RuntimeException("Illegal yType");
                    }

                    switch (zType) {
                        case 0 -> {
                            newMinZ = Math.max(minZ, other.minZ);
                            newMaxZ = Math.min(maxZ, other.maxZ);
                        }
                        case 1 -> {
                            newMinZ = Math.max(minZ, other.maxZ + 1);
                            newMaxZ = maxZ;
                        }
                        case 2 -> {
                            newMinZ = minZ;
                            newMaxZ = Math.min(maxZ, other.minZ - 1);
                        }
                        default -> throw new RuntimeException("Illegal zType");
                    }

                    newRanges.add(new Range(newMinX, newMaxX, newMinY, newMaxY, newMinZ, newMaxZ));
                }
            }
        }

        return newRanges.stream().filter(Range::isValid).toList();
    }

    private boolean isValid() {
        return minX <= maxX && minY <= maxY && minZ <= maxZ;
    }

    private Range coalesce(Range other) {
        if (this.minX == other.minX && this.maxX == other.maxX && this.minY == other.minY && this.maxY == other.maxY) {
            if (Math.abs(this.minX - other.maxX) == 1 || Math.abs(this.maxX - other.minX) == 1) {
                return new Range(minX, maxX, minY, maxY, Math.min(this.minZ, other.minZ), Math.max(this.maxZ, other.maxZ));
            }
        } else if (this.minX == other.minX && this.maxX == other.maxX && this.minZ == other.minZ && this.maxZ == other.maxZ) {
            if (Math.abs(this.minY - other.maxY) == 1 || Math.abs(this.maxY - other.minY) == 1) {
                return new Range(minX, maxX, Math.min(this.minY, other.minY), Math.max(this.maxY, other.maxY), minZ, maxZ);
            }
        } else if (this.minY == other.minY && this.maxY == other.maxY && this.minZ == other.minZ && this.maxZ == other.maxZ) {
            if (Math.abs(this.minX - other.maxX) == 1 || Math.abs(this.maxX - other.minX) == 1) {
                return new Range(Math.min(this.minX, other.minX), Math.max(this.maxX, other.maxX), minY, maxY, minZ, maxZ);
            }
        }

        return null;
    }
}
