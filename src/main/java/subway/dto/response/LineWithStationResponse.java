package subway.dto.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

public class LineWithStationResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineWithStationResponse() {
    }

    public LineWithStationResponse(
            final Long id, final String name,
            final String color,
            final List<StationResponse> stations
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineWithStationResponse from(final Line line, final List<Station> stations) {
        if (stations.isEmpty()) {
            return new LineWithStationResponse(line.getId(), line.getName(), line.getColor(), List.of());
        }
        return new LineWithStationResponse(line.getId(), line.getName(), line.getColor(), StationResponse.of(stations));
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
