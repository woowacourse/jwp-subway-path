package subway.dto.response;

import java.util.List;
import subway.domain.line.Line;

public class LineCreateResponse {

    private final Long lineId;
    private final String lineName;
    private final Double extraCharge;
    private final List<Long> stationIds;

    public LineCreateResponse(Long lineId, String lineName, Double extraCharge, List<Long> stationIds) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.extraCharge = extraCharge;
        this.stationIds = stationIds;
    }

    public static LineCreateResponse from(Line line) {
        return new LineCreateResponse(line.getId(), line.getName(), line.getExtraCharge().getValue(), line.getStationIds());
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public Double getExtraCharge() {
        return extraCharge;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}
