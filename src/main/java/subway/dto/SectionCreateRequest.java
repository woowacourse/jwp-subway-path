package subway.dto;

public class SectionCreateRequest {
    private String startStation;
    private String endStation;
    private int distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String startStation, String endStation, int distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
