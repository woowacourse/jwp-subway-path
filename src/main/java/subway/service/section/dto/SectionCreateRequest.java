package subway.service.section.dto;

public class SectionCreateRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final Long lineId;

    public SectionCreateRequest(Long upStationId, Long downStationName, int distance, Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
