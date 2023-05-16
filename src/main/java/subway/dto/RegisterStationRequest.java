package subway.dto;

public class RegisterStationRequest {

    private String newStationName;
    private String baseStationName;
    private String direction;
    private int distance;

    public RegisterStationRequest() {
    }

    public RegisterStationRequest(String newStationName, String baseStationName, String direction, int distance) {
        this.newStationName = newStationName;
        this.baseStationName = baseStationName;
        this.direction = direction;
        this.distance = distance;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
