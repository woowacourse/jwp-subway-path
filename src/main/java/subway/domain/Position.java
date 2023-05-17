package subway.domain;

public enum Position {

    UP,
    DOWN,
    MID,
    NONE;

    public boolean matches(final Position position) {
        return this == position;
    }
}
