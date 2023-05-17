package subway.dto.request;

public class ConnectionRequest {
    private final String connectionType;
    private final Long prevStationId;
    private final Long nextStationId;
    private final int distance;

    public ConnectionRequest(final String connectType, final Long prevStationId, final Long nextStationId, final int distance) {
        this.connectionType = connectType;
        this.prevStationId = prevStationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public String getConnectionType() {
        return connectionType;
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
