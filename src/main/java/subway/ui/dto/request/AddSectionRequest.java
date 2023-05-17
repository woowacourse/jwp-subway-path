package subway.ui.dto.request;

public class AddSectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    private AddSectionRequest(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static AddSectionRequest of(final Long upStationId, final Long downStationId, final int distance) {
        return new AddSectionRequest(upStationId, downStationId, distance);
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
}
