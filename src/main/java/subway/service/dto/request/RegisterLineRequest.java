package subway.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterLineRequest {

    private final String currentStationName;
    private final String nextStationName;
    private final String lineName;
    private final int distance;

    public RegisterLineRequest(@JsonProperty(value = "stationName") String currentStationName,
                               @JsonProperty(value = "nextStationName") String nextStationName,
                               @JsonProperty(value = "lineName") String lineName,
                               @JsonProperty(value = "distance") int distance) {
        this.currentStationName = currentStationName;
        this.nextStationName = nextStationName;
        this.lineName = lineName;
        this.distance = distance;
    }

    public String getCurrentStationName() {
        return currentStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public int getDistance() {
        return distance;
    }
}
