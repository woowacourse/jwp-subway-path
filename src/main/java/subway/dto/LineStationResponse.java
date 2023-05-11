package subway.dto;

import subway.domain.Station;

import java.util.List;

public class LineStationResponse {
    private Long lineId;
    private List<Station> stations;

    public LineStationResponse(Long lineId, List<Station> stations) {
        this.lineId = lineId;
        this.stations = stations;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<Station> getStations() {
        return stations;
    }
}
