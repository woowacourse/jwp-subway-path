package subway.dto.request;

import subway.domain.Direction;

public class CreateSectionRequest {

    private Long origin;
    private Long destination;
    private Integer distance;
    private Direction direction;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long origin, final Long destination, final Integer distance,
            final Direction direction) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.direction = direction;
    }

    public Long getOrigin() {
        return origin;
    }

    public Long getDestination() {
        return destination;
    }

    public Integer getDistance() {
        return distance;
    }

    public Direction getDirection() {
        return direction;
    }
}
