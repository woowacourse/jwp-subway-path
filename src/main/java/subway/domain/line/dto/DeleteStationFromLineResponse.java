package subway.domain.line.dto;

import java.util.List;

public class DeleteStationFromLineResponse {
    private final Long lineId;
    private final String lineName;
    private final List<Long> stationIds;

    public DeleteStationFromLineResponse(Long lineId, String lineName, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stationIds = stationIds;
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
