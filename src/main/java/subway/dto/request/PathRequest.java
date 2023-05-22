package subway.dto.request;

import org.jetbrains.annotations.NotNull;

public class PathRequest {

    @NotNull("출발역은 비어있을 수 없습니다.")
    private Long departureStationId;

    @NotNull("도착역은 비어있을 수 없습니다.")
    private Long arriveStationId;

    public PathRequest(final Long departureStationId, final Long arriveStationId) {
        this.departureStationId = departureStationId;
        this.arriveStationId = arriveStationId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }
}
