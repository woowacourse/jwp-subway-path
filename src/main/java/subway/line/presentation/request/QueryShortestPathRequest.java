package subway.line.presentation.request;

import javax.validation.constraints.NotNull;

public class QueryShortestPathRequest {

    @NotNull(message = "출발역을 설정하지 않았습니다.")
    private final String startStationName;

    @NotNull(message = "도착역을 설정하지 않았습니다.")
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
