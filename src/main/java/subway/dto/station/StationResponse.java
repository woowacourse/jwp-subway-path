package subway.dto.station;

import subway.domain.station.Station;

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
