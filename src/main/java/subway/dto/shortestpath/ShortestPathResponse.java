package subway.dto.shortestpath;

import java.util.List;
import subway.dto.station.StationResponse;

public class ShortestPathResponse {

    private final List<StationResponse> stationResponses;
    private final int totalFare;

    public ShortestPathResponse(final List<StationResponse> stationResponses, final int totalFare) {
        this.stationResponses = stationResponses;
        this.totalFare = totalFare;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
