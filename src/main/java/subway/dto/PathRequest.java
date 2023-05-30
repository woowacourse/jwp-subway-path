package subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {


    @NotNull(message = "출발역 정보를 입력해주세요.")
    private final Long fromStationId;

    @NotNull(message = "도착역 정보를 입력해주세요.")
    private final Long toStationId;

    public PathRequest(final Long fromStationId, final Long toStationId) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
    }

    public Long getFromStationId() {
        return fromStationId;
    }

    public Long getToStationId() {
        return toStationId;
    }
}
