package subway.ui.dto.response;

import subway.domain.station.Station;

public class StationResponse {
    private final Long stationId;
    private final String stationName;

    public StationResponse(final Station station) {
        this.stationId = station.getStationId();
        this.stationName = station.getStationName();
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

}
