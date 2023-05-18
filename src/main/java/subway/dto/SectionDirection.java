package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import subway.domain.vo.Direction;

public class SectionDirection {

    private final Direction direction;

    @JsonCreator
    public SectionDirection(final String direction) {
        this.direction = Direction.convert(direction);
    }

    public Direction getDirection() {
        return direction;
    }
}
