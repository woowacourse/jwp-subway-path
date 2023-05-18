package subway.station.presentation.dto.response;

import subway.station.domain.Station;

public class StationResponse {
    private Long stationId;
    private String name;

    public StationResponse(Long stationId, String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getNameValue());
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

}
