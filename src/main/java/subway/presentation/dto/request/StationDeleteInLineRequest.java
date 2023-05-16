package subway.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationDeleteInLineRequest {

    @NotNull(message = "stationName 은 Null 이면 안됩니다.")
    private final String stationName;

    @JsonCreator
    public StationDeleteInLineRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

}
