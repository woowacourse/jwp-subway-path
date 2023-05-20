package subway.dto.response;

import java.util.List;

public class LineStationResponse {
    private final List<StationResponse> stationResponses;
    private final List<Integer> distances;

    public LineStationResponse(final List<StationResponse> stations, final List<Integer> distances) {
        this.stationResponses = stations;
        this.distances = distances;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public List<Integer> getDistances() {
        return distances;
    }
}
