package subway.dto;

public class StationsRegisterRequest {
    private Long topId;
    private Long bottomId;
    private Integer distance;

    public StationsRegisterRequest(Long topId, Long bottomId, Integer distance) {
        this.topId = topId;
        this.bottomId = bottomId;
        this.distance = distance;
    }

    public Long getTopId() {
        return topId;
    }

    public Long getBottomId() {
        return bottomId;
    }

    public Integer getDistance() {
        return distance;
    }
}
