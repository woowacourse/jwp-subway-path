package subway.dto;

public class SectionRequest {

    private final Long stationId;
    private final Long lineId;
    private final Long upStationId;
    private final int distance;

    public SectionRequest(final Long stationId, final Long lineId, final Long upStationId, final int distance) {
        this.stationId = stationId;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }
}
