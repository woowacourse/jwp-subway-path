package subway.dto.response;

import java.util.List;
import subway.domain.Line;

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
        return new LineCreateResponse(line.getId(), line.getName(), line.getStationIds());
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
