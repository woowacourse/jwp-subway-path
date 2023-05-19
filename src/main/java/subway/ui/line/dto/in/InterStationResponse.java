package subway.ui.line.dto.in;

public class InterStationResponse {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private long distance;

    private InterStationResponse() {
    }

    public InterStationResponse(final Long id, final Long upStationId, final Long downStationId, final long distance) {
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

    public long getDistance() {
        return distance;
    }
}
