package advent.of.code.solutions.y2024.day24;

import lombok.Getter;

class Node {
    @Getter
    private final String Name;

    private Operations Operation = null;
    @Getter
    private Boolean Value = null;
    private Node[] Previous = null;

    public Node(String name) {
        Name = name;
    }

    public void setOperation(String operation) {
        Operation = operationStringToEnum(operation);
    }

    private Operations operationStringToEnum(String operation) {
        return switch (operation) {
            case "AND" -> Operations.AND;
            case "OR" -> Operations.OR;
            case "XOR" -> Operations.XOR;
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }

    public void setPrevious(Node first, Node second) {
        Previous = new Node[2];
        Previous[0] = first;
        Previous[1] = second;
    }

    public boolean verify() {
        return Name != null && (Value != null || Previous != null && Operation != null);
    }

    public void setValue(String value) {
        if (!value.equals("1") && !value.equals("0")) {
            throw new RuntimeException(String.format("Bad value %s", value));
        }

        Value = value.equals("1");
    }

    public boolean doesNotHaveValue() {
        return Value == null;
    }

    public void execute() {
        if (Value != null) {
            throw new RuntimeException("Executed Node with value.");
        }

        if (Previous == null) {
            return;
        }

        if (Previous[0].doesNotHaveValue()) {
            Previous[0].execute();
        }

        if (Previous[1].doesNotHaveValue()) {
            Previous[1].execute();
        }

        Value = switch (Operation) {
            case AND -> Previous[0].Value && Previous[1].Value;
            case OR -> Previous[0].Value || Previous[1].Value;
            case XOR -> Previous[0].Value ^ Previous[1].Value;
        };
    }
}
