package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class LineResponse {

    @Schema(description = "노선 ID")
    private Long id;

    @Schema(description = "노선 이름")
    private String name;

    @Schema(description = "노선 색")
    private String color;

    @Schema(description = "노선의 역 목록")
    private List<StationResponse> stations;

    public LineResponse(final Long id, final String name, final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), generateStations(line.getStations()));
    }

    private static List<StationResponse> generateStations(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toUnmodifiableList());
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
