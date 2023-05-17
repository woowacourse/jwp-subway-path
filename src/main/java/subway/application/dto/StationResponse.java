package subway.application.dto;

import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private String name;

    public StationResponse() {
    }

    public StationResponse(final String name) {
        this.name = name;
    }

    public static List<StationResponse> of(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getName()))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }
}
