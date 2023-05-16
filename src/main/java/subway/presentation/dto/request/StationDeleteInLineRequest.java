package subway.presentation.dto.request;

import javax.validation.constraints.NotNull;

public class StationDeleteInLineRequest {

    @NotNull(message = "stationName 은 Null 이면 안됩니다.")
    private final String stationName;

    public StationDeleteInLineRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

}
