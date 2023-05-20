package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> orderedStations;

    public PathResponse(List<StationResponse> orderedStations) {
        this.orderedStations = orderedStations;
    }

    public List<StationResponse> getOrderedStations() {
        return orderedStations;
    }
}
