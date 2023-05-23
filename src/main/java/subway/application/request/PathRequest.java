package subway.application.request;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank
    private final String startStationName;

    @NotBlank
    private final String endStationName;

    public PathRequest(final String startStationName, final String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    private PathRequest() {
        this(null, null);
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
