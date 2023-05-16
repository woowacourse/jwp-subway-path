package subway.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.domain.core.Line;
import subway.domain.core.Station;

public class LineResponse {

    private final String name;
    private final String color;
    private final List<String> stations;

    public LineResponse(final String name, final String color, final List<String> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        final List<String> names = line.findAllStation().stream()
                .map(Station::getName)
                .collect(toList());
        return new LineResponse(line.getName(), line.getColor(), names);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<String> getStations() {
        return stations;
    }
}
