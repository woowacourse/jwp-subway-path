package subway.service.dto;

public class StationRegisterRequest {

    private String lineName;
    private String currentStationName;
    private String nextStationName;
    private int distance;

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
