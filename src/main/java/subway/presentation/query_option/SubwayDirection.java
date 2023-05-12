package subway.presentation.query_option;

import java.util.Arrays;

public enum SubwayDirection {

    UP, DOWN;

    public static SubwayDirection from(final String input) {
        return Arrays.stream(SubwayDirection.values())
                .filter(value -> input.toUpperCase().equals(value.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 방향입니다."));
    }

}
