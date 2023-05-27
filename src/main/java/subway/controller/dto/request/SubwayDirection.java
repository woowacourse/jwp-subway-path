package subway.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import subway.exception.InvalidDirectionException;

import java.util.Arrays;

public enum SubwayDirection {

    UP("up"), DOWN("down");

    private final String directionName;

    SubwayDirection(final String directionName) {
        this.directionName = directionName;
    }

    @JsonCreator
    public static SubwayDirection from(final String input) {
        return Arrays.stream(SubwayDirection.values())
                .filter(value -> input.equalsIgnoreCase(value.directionName))
                .findFirst()
                .orElseThrow(InvalidDirectionException::new);
    }
}
