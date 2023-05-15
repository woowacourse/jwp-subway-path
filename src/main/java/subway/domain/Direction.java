package subway.domain;

import java.util.Arrays;

public enum Direction {
    LEFT("LEFT"),
    RIGHT("RIGHT");

    private final String value;

    Direction(String value) {
        this.value = value;
    }

    public boolean isSameDirection(String input) {
        return this.value.equals(input);
    }

    public static Direction findDirection(String input) {
        return Arrays.stream(values())
                .filter(direction -> direction.isSameDirection(input))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("일치하는 방향이 존재하지 않습니다."));
    }
}
