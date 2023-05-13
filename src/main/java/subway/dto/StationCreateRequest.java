package subway.dto;

import javax.validation.constraints.NotNull;

public class StationCreateRequest {

    @NotNull(message = "상행 역의 id를 입력해 주세요. 입력값: ${validatedValue}")
    private final Long upStationId;

    @NotNull(message = "하행 역의 id를 입력해 주세요. 입력값: ${validatedValue}")
    private final Long downStationId;

    @NotNull(message = "거리를 입력해 주세요. 입력값: ${validatedValue}")
    private final int distance;

    public StationCreateRequest(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
