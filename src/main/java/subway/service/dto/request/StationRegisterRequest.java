package subway.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationRegisterRequest {

    private final String lineName;
    private final String currentStationName;
    private final String nextStationName;
    private final int distance;

    public StationRegisterRequest(@JsonProperty(value = "lineName") String lineName,
                                  @JsonProperty(value = "currentStationName") String currentStationName,
                                  @JsonProperty(value = "nextStationName") String nextStationName,
                                  @JsonProperty(value = "distance") int distance) {
        this.lineName = lineName;
        this.currentStationName = currentStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getCurrentStationName() {
        return currentStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public int getDistance() {
        return distance;
    }
}
