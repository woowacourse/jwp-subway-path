package subway.dto.request;

import subway.domain.Direction;

public class CreateSectionRequest {

    private Long upStation;
    private Long downStation;
    private Integer distance;
    private Direction direction;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long upStation, final Long downStation, final Integer distance,
            final Direction direction) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.direction = direction;
    }

    public Long getUpStation() {
        return upStation;
    }

    public Long getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Direction getDirection() {
        return direction;
    }
}
