package subway.service.dto;

public class StationRegisterRequest {

    private String lineName;
    private String currentStationName;
    private String nextStationName;
    private int distance;

    private StationRegisterRequest() {
    }

    public StationRegisterRequest(
            final String lineName,
            final String currentStationName,
            final String nextStationName,
            final int distance
    ) {
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
