package subway.dto;

import javax.validation.constraints.NotNull;

public class ShortestPathRequest {

    @NotNull(message = "출발역은 필수 입력값입니다.")
    private final Long srcStationId;
    @NotNull(message = "도착역은 필수 입력값입니다.")
    private final Long dstStationId;

    public ShortestPathRequest(final Long srcStationId, final Long dstStationId) {
        this.srcStationId = srcStationId;
        this.dstStationId = dstStationId;
    }

    public Long getSrcStationId() {
        return srcStationId;
    }

    public Long getDstStationId() {
        return dstStationId;
    }
}
