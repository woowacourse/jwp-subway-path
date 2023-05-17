package subway.dto.request;

public class ConnectRequest {
    private final Long prevStationId;
    private final Long nextStationId;
    private final int distance;

    public ConnectRequest(final Long prevStationId, final Long nextStationId, final int distance) {
        this.prevStationId = prevStationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public Long getPrevStationId() {
        return prevStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public int getDistance() {
        return distance;
    }
}
