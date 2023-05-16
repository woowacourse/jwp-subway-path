package subway.domain.subwayMap.dto;

import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.station.domain.Station;

import java.util.List;

public class SubwayMapForLineResponse {

    private LineDetailResponse lineDetailResponse;
    private List<Station> stations;

    private SubwayMapForLineResponse() {
    }

    public SubwayMapForLineResponse(LineDetailResponse lineDetailResponse, final List<Station> stations) {
        this.lineDetailResponse = lineDetailResponse;
        this.stations = stations;
    }

    public LineDetailResponse getLineDetailResponse() {
        return lineDetailResponse;
    }

    public List<Station> getStations() {
        return stations;
    }
}
