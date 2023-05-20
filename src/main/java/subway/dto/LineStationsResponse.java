package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.subway.Line;
import subway.domain.subway.Station;

public class LineStationsResponse {
    private final LineResponse line;
    private final List<StationResponse> lineStations;

    private LineStationsResponse(Line line, List<Station> lineStations) {
        this.line = LineResponse.of(line);
        this.lineStations = lineStations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toUnmodifiableList());
    }

    public static LineStationsResponse of(Line line, List<Station> lineStations) {
        return new LineStationsResponse(line, lineStations);
    }

    public LineResponse getLine() {
        return line;
    }

    public List<StationResponse> getLineStations() {
        return lineStations;
    }
}
