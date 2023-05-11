package subway.service.section.dto;

public class SectionCreateRequest {
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public SectionCreateRequest(long upStationId, long downStationName, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationName;
        this.distance = distance;
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
}
