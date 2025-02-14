package advent.of.code.solutions.y2021.day16;

import java.util.Collections;
import java.util.List;

class Packet {
    private final int Version;
    private final int TypeId;
    private final PacketTypes PacketType;

    private final Long Value;

    private final List<Packet> Next;

    public Packet(int version, int typeId, long value) {
        if (typeId != 4) {
            throw new RuntimeException("Used literal constructor with typeId != 4.");
        }

        Version = version;
        TypeId = typeId;
        PacketType = getPacketType(TypeId);
        Value = value;

        Next = null;
    }

    public Packet(int version, int typeId, List<Packet> next) {
        Version = version;
        TypeId = typeId;
        PacketType = getPacketType(TypeId);
        Next = Collections.unmodifiableList(next);

        Value = null;
    }

    private PacketTypes getPacketType(int typeId) {
        return switch (typeId) {
            case 0 -> PacketTypes.SUM;
            case 1 -> PacketTypes.PRODUCT;
            case 2 -> PacketTypes.MINIMUM;
            case 3 -> PacketTypes.MAXIMUM;
            case 4 -> PacketTypes.LITERAL;
            case 5 -> PacketTypes.GREATER_THAN;
            case 6 -> PacketTypes.LESS_THAN;
            case 7 -> PacketTypes.EQUAL_TO;
            default -> throw new IllegalStateException("Unexpected value: " + typeId);
        };
    }

    public int sumVersionNumbers() {
        if (Next == null) {
            return Version;
        }

        int total = Next.stream().mapToInt(Packet::sumVersionNumbers).sum();
        total += Version;

        return total;
    }

    public long getResult() {
        switch (PacketType) {
            case SUM -> {
                return Next.stream().mapToLong(Packet::getResult).sum();
            }
            case PRODUCT -> {
                long product = 1;
                for (Packet packet : Next) {
                    product *= packet.getResult();
                }
                return product;
            }
            case MINIMUM -> {
                return Next.stream().mapToLong(Packet::getResult).min().orElseThrow();
            }
            case MAXIMUM -> {
                return Next.stream().mapToLong(Packet::getResult).max().orElseThrow();
            }
            case LITERAL -> {
                return Value;
            }
            case GREATER_THAN -> {
                if (Next.get(0).getResult() > Next.get(1).getResult()) {
                    return 1;
                }

                return 0;
            }
            case LESS_THAN -> {
                if (Next.get(0).getResult() < Next.get(1).getResult()) {
                    return 1;
                }

                return 0;
            }
            case EQUAL_TO -> {
                if (Next.get(0).getResult() == Next.get(1).getResult()) {
                    return 1;
                }

                return 0;
            }
        }

        throw new RuntimeException("Bad getResult()");
    }
}
