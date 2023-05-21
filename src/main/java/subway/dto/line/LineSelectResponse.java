package subway.dto.line;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.dto.station.StationSelectResponse;

public class LineSelectResponse {
    private final Long lineId;
    private final String lineName;
    private final List<StationSelectResponse> stations;

    private LineSelectResponse(Long lineId, String lineName, List<StationSelectResponse> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public static LineSelectResponse from(Line line) {
        List<StationSelectResponse> collect = line.getStations().stream()
                .map(Station::getName)
                .map(StationSelectResponse::new)
                .collect(Collectors.toList());
        return new LineSelectResponse(line.getId(), line.getName(), collect);
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<StationSelectResponse> getStations() {
        return stations;
    }
}
