package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class LineSelectResponse {
    private String name;
    private List<StationSelectResponse> stations;

    private LineSelectResponse() {
    }

    private LineSelectResponse(String name, List<StationSelectResponse> stations) {
        this.name = name;
        this.stations = stations;
    }

    public static LineSelectResponse of(String lineName, List<Station> stations) {
        List<StationSelectResponse> collect = stations.stream()
                .map(Station::getName)
                .map(StationSelectResponse::new)
                .collect(Collectors.toList());
        return new LineSelectResponse(lineName, collect);
    }

    public String getName() {
        return name;
    }

    public List<StationSelectResponse> getStations() {
        return stations;
    }
}
