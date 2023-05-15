package subway.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StationDeleteRequest {

    private final String lineName;
    private final String stationName;

    @JsonCreator
    public StationDeleteRequest(@JsonProperty(value = "lineName") String lineName,
                                @JsonProperty(value = "stationName") String stationName) {
        this.lineName = lineName;
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }
}
