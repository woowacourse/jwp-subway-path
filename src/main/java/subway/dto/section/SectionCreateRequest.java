package subway.dto.section;

public class SectionCreateRequest {
    private String startStationName;
    private String endStationName;
    private int distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String startStationName, String endStationName, int distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
