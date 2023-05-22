package subway.dto;

public class SubwayPathRequest {

    private long departureId;
    private long destinationId;

    public SubwayPathRequest() {
    }

    public SubwayPathRequest(long departureId, long destinationId) {
        this.departureId = departureId;
        this.destinationId = destinationId;
    }

    @Override
    public String toString() {
        return "SubwayPathRequest{" +
                ", departureId=" + departureId +
                ", destinationId=" + destinationId +
                '}';
    }

    public long getDepartureId() {
        return departureId;
    }

    public long getDestinationId() {
        return destinationId;
    }
}
