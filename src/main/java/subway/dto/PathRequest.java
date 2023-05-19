package subway.dto;

import javax.validation.constraints.NotNull;

public final class PathRequest {
    @NotNull(message = "시작역 id가 필요합니다.")
    private Long upStationId;
    @NotNull(message = "도착역 id가 필요합니다.")
    private Long downStationID;
    @NotNull(message = "거리 정보가 필요합니다.")
    private Integer distance;

    public PathRequest() {
    }

    public PathRequest(final Long upStationId, final Long downStationID, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationID = downStationID;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationID() {
        return downStationID;
    }

    public Integer getDistance() {
        return distance;
    }
}
