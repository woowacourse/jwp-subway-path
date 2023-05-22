package subway.application.dto;

import java.util.List;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final FareResponse fare;

    public RouteResponse(final List<StationResponse> stations, final FareResponse fare) {
        this.stations = stations;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public FareResponse getFare() {
        return fare;
    }
}
