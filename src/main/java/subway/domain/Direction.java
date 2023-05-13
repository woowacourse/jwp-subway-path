package subway.domain;

import java.util.Arrays;

public enum Direction {
    UP,
    DOWN;

    public static Direction of(final String string) {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.name().equalsIgnoreCase(string))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }
}
