package subway.dto.response;

import subway.domain.Station;

import java.util.List;

public class PathResponse {

    private int fare;
    private List<StationResponse> stations;

    private PathResponse() {
    }

    private PathResponse(final Integer fare, final List<StationResponse> stations) {
        this.fare = fare;
        this.stations = stations;
    }

    public static PathResponse of(final Integer fare, final List<Station> stations) {
        return new PathResponse(fare, StationResponse.from(stations));
    }

    public Integer getFare() {
        return fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
