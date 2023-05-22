package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConnectionMidRequest {
    @Positive
    @NotNull
    private final Long prevStationId;
    @Positive
    @NotNull
    private final int distance;

    public ConnectionMidRequest(final Long prevStationId, final int distance) {
        this.prevStationId = prevStationId;
        this.distance = distance;
    }

    public Long getPrevStationId() {
        return prevStationId;
    }

    public int getDistance() {
        return distance;
    }
}
