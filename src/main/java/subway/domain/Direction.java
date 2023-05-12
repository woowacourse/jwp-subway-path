package subway.domain;

public enum Direction {

    UP,
    DOWN;

    public static Direction from(final boolean value) {
        if (value) {
            return UP;
        }
        return DOWN;
    }
}
