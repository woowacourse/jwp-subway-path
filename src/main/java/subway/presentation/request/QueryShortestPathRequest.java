package subway.presentation.request;

public class QueryShortestPathRequest {

    private final String startStationName;
    private final String endStationName;

    public QueryShortestPathRequest(final String startStationName, final String endStationName) {
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
