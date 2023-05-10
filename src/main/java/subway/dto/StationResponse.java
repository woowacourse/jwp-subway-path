package subway.dto;

import subway.domain.Station;

public class StationResponse {
    private String name;

    public StationResponse(String name) {
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getName());
    }

    public String getName() {
        return name;
    }
}
