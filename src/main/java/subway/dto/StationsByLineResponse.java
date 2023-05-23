package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class StationsByLineResponse {

    private Long lineId;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public StationsByLineResponse() {
    }

    public StationsByLineResponse(Long lineId, String name, String color, List<StationResponse> stations) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public StationsByLineResponse(final Line line, final List<Station> stations) {
        this.lineId = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getLineId() {
        return lineId;
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
