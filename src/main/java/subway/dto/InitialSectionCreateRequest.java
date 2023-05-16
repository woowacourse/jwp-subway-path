package subway.dto;

import javax.validation.constraints.NotNull;

public class InitialSectionCreateRequest {

    @NotNull(message = "노선 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long lineId;

    @NotNull(message = "상행 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long upStationId;

    @NotNull(message = "하행 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long downStationId;

    @NotNull(message = "역간 거리를 입력해 주세요. 입력값 : ${validatedValue}")
    private final int distance;

    public InitialSectionCreateRequest(
            final Long lineId,
            final Long upStationId,
            final Long downStationId,
            final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
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
