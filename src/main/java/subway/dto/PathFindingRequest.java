package subway.dto;

public class PathFindingRequest {
    private Long departureId;
    private Long destinationId;
    private int age;

    public PathFindingRequest() {
    }

    public PathFindingRequest(Long departureId, Long destinationId, int age) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.age = age;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public int getAge() {
        return this.age;
    }
}
