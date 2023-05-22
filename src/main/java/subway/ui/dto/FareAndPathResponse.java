package subway.ui.dto;

import java.util.List;

import subway.domain.Station;

public class FareAndPathResponse {

    private final int fare;
    private final List<StationResponse> stations;

    private FareAndPathResponse(int fare, List<StationResponse> stations) {
        this.fare = fare;
        this.stations = stations;
    }

    public static FareAndPathResponse from(int fare, List<Station> stations) {
        return new FareAndPathResponse(fare, StationResponse.toStationResponses(stations));
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
