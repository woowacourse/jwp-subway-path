package subway.dto.response;

import java.util.List;
import subway.domain.line.Line;

public class AddStationToLineResponse {
    private final long lineId;
    private final String lineName;
    private final List<Long> stationIds;

    public AddStationToLineResponse(long lineId, String lineName, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stationIds = stationIds;
    }

    public static AddStationToLineResponse from(Line line) {
        return new AddStationToLineResponse(line.getId(), line.getName(), line.getStationIds());
    }

    public long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}
