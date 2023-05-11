package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StationDeleteRequest {
    private final String station;

    @JsonCreator
    public StationDeleteRequest(@JsonProperty("station") String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }
}
