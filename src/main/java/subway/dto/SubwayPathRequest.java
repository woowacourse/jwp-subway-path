package subway.dto;

public class SubwayPathRequest {

    private long lineId;
    private long departureId;
    private long destinationId;

    public SubwayPathRequest() {
    }

    public SubwayPathRequest(long lineId, long departureId, long destinationId) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.lineId = lineId;
    }

    @Override
    public String toString() {
        return "SubwayPathRequest{" +
                "lineId=" + lineId +
                ", departureId=" + departureId +
                ", destinationId=" + destinationId +
                '}';
    }

    public long getLineId() {
        return lineId;
    }

    public long getDepartureId() {
        return departureId;
    }

    public long getDestinationId() {
        return destinationId;
    }
}
