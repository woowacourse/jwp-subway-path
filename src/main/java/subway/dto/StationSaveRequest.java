package subway.dto;

import subway.domain.Direction;

public class StationSaveRequest {

    private final String lineName;
    private final String baseStationName;
    private final String additionalStationName;
    private final Direction direction;
    private final Integer distance;

    public StationSaveRequest(
            final String lineName,
            final String baseStationName,
            final String additionalStationName,
            final Direction direction,
            final Integer distance
    ) {
        this.lineName = lineName;
        this.baseStationName = baseStationName;
        this.additionalStationName = additionalStationName;
        this.direction = direction;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getAdditionalStationName() {
        return additionalStationName;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
