package subway.dto;

public class StationRequest {

    private String name;
    private String nextStationName;
    private int distance;

    public StationRequest() {
    }

    public StationRequest(String name, String nextStationName, int distance) {
        this.name = name;
        this.distance = distance;
        this.nextStationName = nextStationName;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public String getNextStationName() {
        return nextStationName;
    }
}
