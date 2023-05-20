package subway.dto.request;

import subway.ui.EndpointType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConnectionEndpointRequest {
    @NotNull
    private final EndpointType connectionType;
    @Positive
    @NotNull
    private final int distance;

    public ConnectionEndpointRequest(final EndpointType connectType, final int distance) {
        this.connectionType = connectType;
        this.distance = distance;
    }

    public EndpointType getConnectionType() {
        return connectionType;
    }

    public int getDistance() {
        return distance;
    }
}
