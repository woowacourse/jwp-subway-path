package subway.dto;

import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<String> stationResponses;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(final Long id, final String name, final String color, final List<String> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(Line line) {
        try {
            return new LineResponse(line.getId(), line.getName(), line.getColor(),
                    line.getRoute().stream()
                            .map(station -> station.getName())
                            .collect(Collectors.toList()));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return new LineResponse(line.getId(), line.getName(), line.getColor());
        }
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

    public List<String> getStationResponses() {
        return stationResponses;
    }
}
