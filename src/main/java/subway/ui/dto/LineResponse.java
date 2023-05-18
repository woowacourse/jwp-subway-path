package subway.ui.dto;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    private LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(final Line line, final List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
