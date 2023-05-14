package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineCreateResponse {

    private final Long lineId;
    private final String lineName;
    private final List<Long> stationIds;

    private LineCreateResponse(Long lineId, String lineName, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stationIds = stationIds;
    }

    public static LineCreateResponse fromDomain(Line line) {
        List<Long> stationIds = line.getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return new LineCreateResponse(line.getId(), line.getName(), stationIds);
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}
