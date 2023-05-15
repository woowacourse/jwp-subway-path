package subway.dto.response;

import subway.domain.Station;

import java.util.List;

public class StationsResponse {
    private List<Station> stations;

    public StationsResponse() {
    }

    public StationsResponse(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
