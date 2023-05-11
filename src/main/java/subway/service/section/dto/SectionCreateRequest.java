package subway.service.section.dto;

public class SectionCreateRequest {
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final long lineId;

    public SectionCreateRequest(long upStationId, long downStationName, int distance, long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }
}
