package subway.domain;

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
}
