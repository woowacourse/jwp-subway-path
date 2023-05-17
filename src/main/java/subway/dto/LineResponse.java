package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {
    private final String name;
    private final List<StationResponse> stations;

    public LineResponse(String name, List<StationResponse> stations) {
        this.name = name;
        this.stations = stations;
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stations = line.getStations().stream()
                .map(Station::getName)
                .map(StationResponse::new)
                .collect(Collectors.toList());
        return new LineResponse(line.getName(), stations);
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
