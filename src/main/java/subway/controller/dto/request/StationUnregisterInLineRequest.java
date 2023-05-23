package subway.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class StationUnregisterInLineRequest {

    @NotBlank(message = "stationName 이 비어있습니다.")
    private final String stationName;

    @JsonCreator
    public StationUnregisterInLineRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
