package subway.dto;

public class LineNodeRequest {

    private final String beforeStationName;
    private final String stationName;
    private final Integer distance;

    private LineNodeRequest() {
        this(null, null, null);
    }

    public LineNodeRequest(final String beforeStationName, final String stationName, final Integer distance) {
        this.beforeStationName = beforeStationName;
        this.stationName = stationName;
        this.distance = distance;
    }

    public String getBeforeStationName() {
        return beforeStationName;
    }

    public String getStationName() {
        return stationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
