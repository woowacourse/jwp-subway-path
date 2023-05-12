package subway.ui.dto.response;

import subway.station.domain.Station;

public class StationResponse {
    private final String stationName;

    public StationResponse(final Station station) {
        this.stationName = station.getStationName();
    }

    public String getStationName() {
        return stationName;
    }
}
