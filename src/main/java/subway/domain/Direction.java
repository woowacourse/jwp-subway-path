package subway.domain;

public enum Direction {
    LEFT,
    RIGHT,
    ;

    public Direction flip() {
        if (this == LEFT) {
            return RIGHT;
        }
        return LEFT;
    }
}
