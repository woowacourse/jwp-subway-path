package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class RemoveStationRequest {
    private final String station;

    @JsonCreator
    public RemoveStationRequest(final String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }
}
