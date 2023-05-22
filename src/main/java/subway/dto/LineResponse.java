package subway.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {

    private String name;
    private String color;
    private List<String> stations;

    private LineResponse() {
    }

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
