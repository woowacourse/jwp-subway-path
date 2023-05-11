package subway.service.dto;

public class RegisterLineRequest {

    private String currentStationName;
    private String nextStationName;
    private String lineName;
    private int distance;

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
