package subway.domain.graph;

public enum Direction {

    UP,
    DOWN;

    public static Direction from(final boolean value) {
        if (value) {
            return UP;
        }
        return DOWN;
    }

    public Direction getOpposite() {
        if (this == UP) {
            return DOWN;
        }
        return UP;
    }
}
