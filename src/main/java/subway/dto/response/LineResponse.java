package subway.dto.response;

import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private final String name;
    private final String color;
    private final List<String> stations;

    private LineResponse(final String name, final String color, final List<String> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(final Line line, final List<String> stations) {
        return new LineResponse(line.getName(), line.getColor(), stations);
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
