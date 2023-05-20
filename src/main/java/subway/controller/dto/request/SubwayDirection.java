package subway.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import subway.exception.InvalidDirectionException;

import java.util.Arrays;

public enum SubwayDirection {

    UP, DOWN;

    @JsonCreator
    public static SubwayDirection from(final String input) {
        return Arrays.stream(SubwayDirection.values())
                .filter(value -> input.equalsIgnoreCase(value.name()))
                .findFirst()
                .orElseThrow(InvalidDirectionException::new);
    }
}
