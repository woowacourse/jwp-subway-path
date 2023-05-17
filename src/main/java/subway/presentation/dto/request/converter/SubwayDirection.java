package subway.presentation.dto.request.converter;

import subway.exception.bad_request.InvalidDirectionException;

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
