package subway.controller.dto.request;

public class FindShortestPathRequest {

    private String startStationName;
    private String endStationName;

    private FindShortestPathRequest() {
    }

    public FindShortestPathRequest(final String startStationName, final String endStationName) {
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
