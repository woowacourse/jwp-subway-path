package subway.dto;

import subway.domain.Distance;

public class StationsSavingRequest {
    String previousStationName;
    String nextStationName;
    Distance distance;
    boolean isDown;

    public StationsSavingRequest() {
    }

    public StationsSavingRequest(String previousStationName, String nextStationName, int distance, boolean isDown) {
        this.previousStationName = previousStationName;
        this.nextStationName = nextStationName;
        this.distance = Distance.of(distance);
        this.isDown = isDown;
    }

    public String getPreviousStationName() {
        return previousStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean isDown() {
        return isDown;
    }
}
