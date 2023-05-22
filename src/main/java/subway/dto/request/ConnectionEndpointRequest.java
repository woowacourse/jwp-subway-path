package subway.dto.request;

import subway.ui.EndpointType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ConnectionEndpointRequest {
    @NotNull
    private final String endpointType;
    @Positive
    @NotNull
    private final int distance;

    public ConnectionEndpointRequest(final String connectType, final int distance) {
        this.endpointType = connectType;
        this.distance = distance;
    }

    public EndpointType getEndpointType() {
        return EndpointType.fromValue(endpointType);
    }

    public int getDistance() {
        return distance;
    }
}
