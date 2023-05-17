package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import subway.domain.Station;

public class RouteResponse {

    private final List<Station> stations;

    @JsonCreator
    public RouteResponse(final List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
