package subway.domain.line.domain;

public enum Direction {

    UP(true),
    DOWN(false);

    private final boolean value;

    Direction(final boolean value) {
        this.value = value;
    }

    public static Direction from(final boolean value) {
        if (value) {
            return UP;
        }
        return DOWN;
    }

    public String reverseDirectionName() {
        if (this.value) {
            return DOWN.name();
        }
        return UP.name();
    }
}
