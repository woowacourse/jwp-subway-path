package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineSelectResponse {
    private Long lineId;
    private String lineName;
    private List<StationSelectResponse> stations;

    private LineSelectResponse(Long lineId, String lineName, List<StationSelectResponse> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public static LineSelectResponse of(Line line) {
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
