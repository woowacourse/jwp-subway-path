package subway.application.dto;

import java.util.List;

public class LineQueryResponse {

    private final String lineName;
    private final List<StationQueryResponse> stationQueryResponseList;

    public LineQueryResponse(final String lineName, final List<StationQueryResponse> stationQueryResponseList) {
        this.lineName = lineName;
        this.stationQueryResponseList = stationQueryResponseList;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationQueryResponse> getStationQueryResponseList() {
        return stationQueryResponseList;
    }

    public static class StationQueryResponse {
        private final String stationName;
        private final int nextStationDistance;

        public StationQueryResponse(final String stationName, final int nextStationDistance) {
            this.stationName = stationName;
            this.nextStationDistance = nextStationDistance;
        }

        public String getStationName() {
            return stationName;
        }

        public int getNextStationDistance() {
            return nextStationDistance;
        }
    }
}
