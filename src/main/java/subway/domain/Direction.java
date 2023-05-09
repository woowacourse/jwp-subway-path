package subway.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Direction {
    UP,
    DOWN,
    ;

    @JsonCreator
    public static Direction from(String string) {
        return Direction.valueOf(string.toUpperCase());
    }
}
