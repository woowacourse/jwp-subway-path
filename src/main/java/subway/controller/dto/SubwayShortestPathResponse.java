package subway.controller.dto;

import java.util.List;

public class SubwayShortestPathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public SubwayShortestPathResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
