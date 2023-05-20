package subway.dto;

public class PathFindingRequest {
    private Long departureId;
    private Long destinationId;

    public PathFindingRequest() {
    }

    public PathFindingRequest(Long departureId, Long destinationId) {
        this.departureId = departureId;
        this.destinationId = destinationId;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}
