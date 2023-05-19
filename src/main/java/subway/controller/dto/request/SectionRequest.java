package subway.controller.dto.request;

public class SectionRequest {

    private final String lineName;
    private final String direction;
    private final String standardStationName;
    private final String additionalStationName;
    private final Integer distance;

    public SectionRequest(final String lineName, final String direction, final String standardStationName,
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

    public String getDirection() {
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
