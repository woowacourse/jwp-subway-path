package subway.domain.vo;

import java.util.Arrays;

public enum Direction {

    UP("up"),
    DOWN("down");

    private final String value;

    Direction(final String value) {
        this.value = value;
    }

    public static Direction convert(String value) {
        return Arrays.stream(values())
                .filter(direction -> direction.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바른 방향을 입력해주세요.(상행: up, 하행: down)"));
    }
}
