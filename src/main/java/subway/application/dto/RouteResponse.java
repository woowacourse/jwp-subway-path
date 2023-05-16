package subway.application.dto;

import java.util.List;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final int fare;

    public RouteResponse(final List<StationResponse> stations, final int fare) {
        this.stations = stations;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }
}
