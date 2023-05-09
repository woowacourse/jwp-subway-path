package subway.dto;

import java.util.List;
import subway.domain.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    // TODO: stations를 올바르게 변경
    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of());
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
