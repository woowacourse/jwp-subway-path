package subway.domain.section;

public enum Direction {

    UP,
    DOWN;

    public Direction reverse() {
        if (this == Direction.UP) {
            return DOWN;
        }
        return UP;
    }

    public boolean matches(final Direction direction) {
        return this == direction;
    }
}
