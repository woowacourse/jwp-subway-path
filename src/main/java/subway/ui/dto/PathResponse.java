package subway.ui.dto;

import java.util.List;

import subway.domain.Station;

public class PathResponse {

    private final int fare;
    private final List<StationResponse> stations;

    private PathResponse(int fare, List<StationResponse> stations) {
        this.fare = fare;
        this.stations = stations;
    }

    public static PathResponse from(int fare, List<Station> stations) {
        return new PathResponse(fare, StationResponse.toStationResponses(stations));
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
