package subway.service.dto;

import subway.controller.dto.request.SubwayDirection;

public class SectionInsertDto {

    private final String lineName;
    private final SubwayDirection direction;
    private final String standardStationName;
    private final String newStationName;
    private final Integer distance;

    public SectionInsertDto(final String lineName, final SubwayDirection direction, final String standardStationName,
                            final String newStationName, final Integer distance) {
        this.lineName = lineName;
        this.direction = direction;
        this.standardStationName = standardStationName;
        this.newStationName = newStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public SubwayDirection getDirection() {
        return direction;
    }

    public String getStandardStationName() {
        return standardStationName;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public Integer getDistance() {
        return distance;
    }

}
