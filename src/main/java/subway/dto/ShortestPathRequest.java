package subway.dto;

public class ShortestPathRequest {

    private Long upStationId;
    private Long downStationId;

    public ShortestPathRequest(final Long upStationId, final Long downStationId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
