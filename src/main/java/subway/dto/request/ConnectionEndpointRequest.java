package subway.dto.request;

import subway.ui.EndpointType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConnectionEndpointRequest {
    @NotNull
    private final EndpointType endpointType;
    @Positive
    @NotNull
    private final int distance;

    public ConnectionEndpointRequest(final EndpointType connectType, final int distance) {
        this.endpointType = connectType;
        this.distance = distance;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public int getDistance() {
        return distance;
    }
}
