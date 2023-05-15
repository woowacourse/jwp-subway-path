package subway.dto;

import java.util.List;

import subway.domain.Line;
import subway.domain.Station;

public class LineStationsResponse {
    private final Line line;
    private final List<Station> lineStations;

    private LineStationsResponse(Line line, List<Station> lineStations) {
        this.line = line;
        this.lineStations = lineStations;
    }

    public static LineStationsResponse of(Line line, List<Station> lineStations) {
        return new LineStationsResponse(line, lineStations);
    }

    public Line getLine() {
        return line;
    }

    public List<Station> getLineStations() {
        return lineStations;
    }
}
