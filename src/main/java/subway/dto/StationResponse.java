package subway.dto;

import subway.domain.Station;

public class StationResponse {
    private String stationName;

    public StationResponse(String stationName) {
        this.stationName = stationName;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getName());
    }

    public String getStationName() {
        return stationName;
    }
}
