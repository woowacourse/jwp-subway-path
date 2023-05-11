package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class StationsResponse {
    private final List<StationResponse> stations;

    private StationsResponse(final List<StationResponse> stations) {
        this.stations = stations;
    }

    public static StationsResponse from(final List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new StationsResponse(stationResponses);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
