package subway.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationUnregisterInLineRequest {

    @NotNull(message = "stationName 은 null 이면 안됩니다.")
    private final String stationName;

    @JsonCreator
    public StationUnregisterInLineRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

}
