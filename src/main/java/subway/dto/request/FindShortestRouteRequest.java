package subway.dto.request;

public class FindShortestRouteRequest {
    private final Integer passengerAge;
    private final Long startStationId;
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
