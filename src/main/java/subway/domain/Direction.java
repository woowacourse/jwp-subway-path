package subway.domain;

public enum Direction {
    UP_BOUND,
    DOWN_BOUND;

    public static Direction changeDirection(Direction direction) {
        if (direction == UP_BOUND) {
            return DOWN_BOUND;
        }
        return UP_BOUND;
    }
}
