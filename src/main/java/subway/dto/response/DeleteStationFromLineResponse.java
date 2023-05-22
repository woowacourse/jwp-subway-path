package subway.dto.response;

import java.util.List;
import subway.domain.line.Line;

public class DeleteStationFromLineResponse {
    private final Long lineId;
    private final String lineName;
    private final List<Long> stationIds;

    private DeleteStationFromLineResponse(Long lineId, String lineName, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stationIds = stationIds;
    }

    public static DeleteStationFromLineResponse from(Line line) {
        return new DeleteStationFromLineResponse(line.getId(), line.getName(), line.getStationIds());
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
