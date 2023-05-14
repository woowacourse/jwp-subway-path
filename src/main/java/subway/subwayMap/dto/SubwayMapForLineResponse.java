package subway.subwayMap.dto;

import subway.line.dto.LineResponse;
import subway.station.domain.Station;

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
