package subway.dto;

public class SectionSaveRequest {
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public SectionSaveRequest(final long upStationId, final long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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
