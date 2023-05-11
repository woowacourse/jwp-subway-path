package subway.dto;

public class SectionRequest {

    private Long fromId;
    private Long toId;
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(final Long fromId, final Long toId, final Integer distance) {
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
