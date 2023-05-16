package subway.dto;

public final class RegisterLastStationRequest {

    private String baseStationName;
    private String newStationName;
    private int distance;

    public RegisterLastStationRequest() {
    }

    public RegisterLastStationRequest(String baseStationName, String newStationName, int distance) {
        this.baseStationName = baseStationName;
        this.newStationName = newStationName;
        this.distance = distance;
    }

    public String getBaseStationName() {
        return baseStationName;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public int getDistance() {
        return distance;
    }
}
