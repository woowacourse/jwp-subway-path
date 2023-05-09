package subway.dto;

public class SectionRequest {

    private final Long stationId;
    private final Long lineId;
    private final Long upStationId;

    public SectionRequest(final Long stationId, final Long lineId, final Long upStationId) {
        this.stationId = stationId;
        this.lineId = lineId;
        this.upStationId = upStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }
}
