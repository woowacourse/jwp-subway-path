package subway.dto;

import subway.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                new ArrayList<>()
        );
    }

    public static LineResponse of(final Line line, final List<StationResponse> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stations
        );
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
        return List.copyOf(stations);
    }
}
