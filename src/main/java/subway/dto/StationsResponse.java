package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class StationsResponse {
    private List<StationResponse> stations;

    private StationsResponse() {
    }

    public StationsResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public static StationsResponse from(List<Station> stations) {
        List<StationResponse> collect = stations.stream()
                .map(station -> station.getName())
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return new StationsResponse(collect);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    static class StationResponse {

        private String name;

        private StationResponse() {
        }

        public StationResponse(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
