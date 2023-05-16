package subway.service.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank
    private final String startStationName;
    @NotBlank
    private final String endStationName;

    private PathRequest() {
        this(null, null);
    }

    public PathRequest(final String startStationName, final String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }
}
