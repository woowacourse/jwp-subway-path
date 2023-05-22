package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class FindShortestRouteRequest {
    @NotNull(message = "승객의 나이는 비어있을 수 없습니다.")
    @Positive(message = "승객의 나이는 양수여야 합니다.")
    private final Integer passengerAge;

    @NotNull(message = "출발역은 비어있을 수 없습니다.")
    private final Long startStationId;

    @NotNull(message = "도착역은 비어있을 수 없습니다.")
    private final Long endStationId;

    public FindShortestRouteRequest(Integer passengerAge, Long startStationId, Long endStationId) {
        this.passengerAge = passengerAge;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Integer getPassengerAge() {
        return passengerAge;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
