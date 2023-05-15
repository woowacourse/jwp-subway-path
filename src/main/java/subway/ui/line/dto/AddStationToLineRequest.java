package subway.ui.line.dto;

import subway.domain.edge.Direction;

public class AddStationToLineRequest {

    private final Long existStationId;
    private final String newStationName;
    private final Direction direction;
    private final Integer distance;

    public AddStationToLineRequest(final Long existStationId, final String newStationName, final Direction direction, final Integer distance) {
        this.existStationId = existStationId;
        this.newStationName = newStationName;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getExistStationId() {
        return existStationId;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
