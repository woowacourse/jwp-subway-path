package subway.dto;

public class StationAddRequest {

    private Long fromId;
    private Long toId;
    private Integer distance;

    public StationAddRequest() {
    }

    public StationAddRequest(final Long fromId, final Long toId, final Integer distance) {
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public Integer getDistance() {
        return distance;
    }
}
