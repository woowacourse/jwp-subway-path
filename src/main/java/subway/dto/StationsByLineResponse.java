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

    public StationsByLineResponse(final Line line, final List<Station> stations) {
        this.lineId = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse getLineResponse() {
        return new LineResponse(lineId, name, color);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
