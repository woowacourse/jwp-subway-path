package subway.dto;

public class RegisterStationRequest {

    private String newStationName;
    private String baseStation;
    private String direction;
    private int distance;

    public RegisterStationRequest() {
    }

    public RegisterStationRequest(String newStationName, String baseStation, String direction, int distance) {
        this.newStationName = newStationName;
        this.baseStation = baseStation;
        this.direction = direction;
        this.distance = distance;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
