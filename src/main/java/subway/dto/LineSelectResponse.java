package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class LineSelectResponse {
    private List<StationSelectResponse> stations;

    private LineSelectResponse() {
    }

    public LineSelectResponse(List<StationSelectResponse> stations) {
        this.stations = stations;
    }

    public static LineSelectResponse from(List<Station> stations) {
        List<StationSelectResponse> collect = stations.stream()
                .map(Station::getName)
                .map(StationSelectResponse::new)
                .collect(Collectors.toList());
        return new LineSelectResponse(collect);
    }

    public List<StationSelectResponse> getStations() {
        return stations;
    }
}
