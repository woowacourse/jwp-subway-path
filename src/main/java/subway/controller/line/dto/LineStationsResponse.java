package subway.controller.line.dto;

import subway.controller.station.dto.StationResponse;

import java.util.List;

public class LineStationsResponse {
    private long id;
    private List<StationResponse> stations;

    public LineStationsResponse(long id, List<StationResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public LineStationsResponse() {
    }

    public long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
