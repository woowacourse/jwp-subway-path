package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConnectionInitRequest {
    @Positive
    @NotNull
    private final Long nextStationId;
    @Positive
    @NotNull
    private final int distance;

    public ConnectionInitRequest(final Long nextStationId, final int distance) {
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public int getDistance() {
        return distance;
    }
}
