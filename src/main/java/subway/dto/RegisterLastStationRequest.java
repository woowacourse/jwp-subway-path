package subway.dto;

public final class RegisterLastStationRequest {

    private String baseStation;
    private String newStationName;
    private int distance;

    public RegisterLastStationRequest() {
    }

    public RegisterLastStationRequest(String baseStation, String newStationName, int distance) {
        this.baseStation = baseStation;
        this.newStationName = newStationName;
        this.distance = distance;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getNewStationName() {
        return newStationName;
    }

    public int getDistance() {
        return distance;
    }
}
