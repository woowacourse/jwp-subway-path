package subway.controller.line.dto;

import subway.controller.station.dto.StationWebResponse;

import java.util.List;

public class LineStationsResponse {
    private long id;
    private List<StationWebResponse> stations;

    public LineStationsResponse(long id, List<StationWebResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public LineStationsResponse() {
    }

    public long getId() {
        return id;
    }

    public List<StationWebResponse> getStations() {
        return stations;
    }
}
