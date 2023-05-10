package subway.dto;

public class StationsSavingRequest {
    String previousStationName;
    String nextStationName;
    int distance;
    boolean isDown;

    public StationsSavingRequest() {
    }

    public StationsSavingRequest(String previousStationName, String nextStationName, int distance, boolean isDown) {
        this.previousStationName = previousStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
        this.isDown = isDown;
    }

    public String getPreviousStationName() {
        return previousStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isDown() {
        return isDown;
    }
}
