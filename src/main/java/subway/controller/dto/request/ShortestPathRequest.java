package subway.controller.dto.request;

import javax.validation.constraints.NotBlank;

public class ShortestPathRequest {

    @NotBlank(message = "startStationName 이 blank 면 안됩니다.")
    private final String startStationName;
    @NotBlank(message = "endStationName 이 blank 면 안됩니다.")
    private final String endStationName;

    public ShortestPathRequest(final String startStationName, final String endStationName) {
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
