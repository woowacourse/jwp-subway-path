package subway.dto;

import subway.domain.Line;
import subway.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponses;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(LineEntity line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static LineResponse of(Line line) {
        if (line.getSections().getSections().size() > 0) {
            return new LineResponse(line.getId(), line.getName(), line.getColor(),
                    line.getRoute().stream()
                            .map(station -> StationResponse.of(station))
                            .collect(Collectors.toList()));
        }
        return new LineResponse(line.getId(), line.getName(), line.getColor());
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

    @Override
    public String toString() {
        return "LineResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stationResponses=" + stationResponses +
                '}';
    }
}
