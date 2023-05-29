package subway.domain.section;

public enum Direction {
    LEFT, RIGHT;

    public Direction reverse() {
        if (this == LEFT) {
            return RIGHT;
        }
        return LEFT;
    }
}
