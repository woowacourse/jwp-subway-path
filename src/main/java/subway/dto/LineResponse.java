package subway.dto;

import java.util.List;
import subway.domain.line.Line;
import subway.entity.LineEntity;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponses;

    public LineResponse(Long id, String name, String color, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), List.of());
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

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
