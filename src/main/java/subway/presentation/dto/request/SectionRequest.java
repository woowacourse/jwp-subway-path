package subway.presentation.dto.request;

public class SectionRequest {

    private final String lineName;
    private final String direction;
    private final String standardStationName;
    private final String newStationName;
    private final Integer distance;

    public SectionRequest(final String lineName, final String direction, final String standardStationName,
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

    public String getDirection() {
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
