package subway.presentation.query_option;

import subway.exception.InvalidDirectionException;

import java.util.Arrays;

public enum SubwayDirection {

    UP, DOWN;

    public static SubwayDirection from(final String input) {
        return Arrays.stream(SubwayDirection.values())
                .filter(value -> input.toUpperCase().equals(value.name()))
                .findFirst()
                .orElseThrow(InvalidDirectionException::new);
    }

}
