package subway.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationDeleteRequest {

    private final String lineName;
    private final String stationName;

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
