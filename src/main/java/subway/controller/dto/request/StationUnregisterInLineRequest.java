package subway.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationUnregisterInLineRequest {

    @NotNull(message = "stationName 이 null 입니다.")
    private final Long stationId;

    @JsonCreator
    public StationUnregisterInLineRequest(final Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
