package subway.domain;

import java.util.Arrays;

public enum Direction {
    UP("up"),
    DOWN("down");

    private final String value;

    Direction(final String value) {
        this.value = value;
    }

    public static Direction getDirection(final String name) {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.value.equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("방향의 이름이 올바르지 않습니다."));
    }

    public String getValue() {
        return value;
    }
}
