package subway.dto.response;

import java.util.List;
import subway.domain.Line;

public class LineCreateResponse {

    private final Long lineId;
    private final String lineName;
    private final Integer extraCharge;
    private final List<Long> stationIds;

    public LineCreateResponse(Long lineId, String lineName, Integer extraCharge, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.extraCharge = extraCharge;
        this.stationIds = stationIds;
    }

    public static LineCreateResponse fromDomain(Line line) {
        return new LineCreateResponse(line.getId(), line.getName(), line.getExtraCharge(), line.getStationIds());
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getExtraCharge() {
        return extraCharge;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}
