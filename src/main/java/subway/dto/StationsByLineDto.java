package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class StationsByLineDto {
    private LineResponse lineResponse;
    private List<StationResponse> stations;

    public StationsByLineDto() {
    }

    public StationsByLineDto(final Line line, final List<Station> stations) {
        this.lineResponse = new LineResponse(line.getId(), line.getName(), line.getColor());
        this.stations = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
