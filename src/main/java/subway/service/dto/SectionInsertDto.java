package subway.service.dto;

import subway.service.domain.Direction;

public class SectionInsertDto {

    private final String lineName;
    private final Direction direction;
    private final String standardStationName;
    private final String additionalStationName;
    private final Integer distance;

    public SectionInsertDto(final String lineName, final Direction direction, final String standardStationName,
                            final String additionalStationName, final Integer distance) {
        this.lineName = lineName;
        this.direction = direction;
        this.standardStationName = standardStationName;
        this.additionalStationName = additionalStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getStandardStationName() {
        return standardStationName;
    }

    public String getAdditionalStationName() {
        return additionalStationName;
    }

    public Integer getDistance() {
        return distance;
    }

}
