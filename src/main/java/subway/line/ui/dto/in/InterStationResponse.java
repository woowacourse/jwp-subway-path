package subway.line.ui.dto.in;

public class InterStationResponse {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private long distance;

    private InterStationResponse() {
    }

    public InterStationResponse(Long id, Long upStationId, Long downStationId, long distance) {
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
