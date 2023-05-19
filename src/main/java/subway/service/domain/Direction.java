package subway.service.domain;

import java.util.Arrays;

public enum Direction {

    UP, DOWN;

    public static Direction from(final String input) {
        return Arrays.stream(Direction.values())
                .filter(value -> input.toUpperCase().equals(value.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 방향입니다."));
    }

}
