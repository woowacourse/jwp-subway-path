package subway.dto;

public class ShortestPathRequest {

    private String startStationName;
    private String endStationName;

    private ShortestPathRequest() {
    }

    public ShortestPathRequest(final String startStationName, final String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
