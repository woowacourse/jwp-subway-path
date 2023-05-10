package subway.dto;

public class SectionRequest {

    private final Long lineId;
    private final Long upStationId;
    private final Long stationId;
    private final int distance;

    public SectionRequest(final Long lineId, final Long upStationId, final Long stationId, final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.stationId = stationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }
}
