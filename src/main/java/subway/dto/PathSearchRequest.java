package subway.dto;

public class PathSearchRequest {

    private final Long departureStationId;
    private final Long arrivalStationId;

    public PathSearchRequest(final Long departureStationId, final Long arrivalStationId) {
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getArrivalStationId() {
        return arrivalStationId;
    }
}
