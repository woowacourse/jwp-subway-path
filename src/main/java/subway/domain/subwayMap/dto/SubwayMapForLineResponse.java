package subway.domain.subwayMap.dto;

import subway.domain.line.dto.LineResponse;
import subway.domain.station.domain.Station;

import java.util.List;

public class SubwayMapForLineResponse {

    private LineResponse lineResponse;
    private List<Station> stations;

    private SubwayMapForLineResponse() {
    }

    public SubwayMapForLineResponse(LineResponse lineResponse, final List<Station> stations) {
        this.lineResponse = lineResponse;
        this.stations = stations;
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<Station> getStations() {
        return stations;
    }
}
