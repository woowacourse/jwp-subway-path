package subway.dto;

public class SectionResponse {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SectionResponse(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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
