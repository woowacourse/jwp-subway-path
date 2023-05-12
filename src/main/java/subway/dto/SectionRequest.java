package subway.dto;

public class SectionRequest {
    private String startStation;
    private String endStation;
    private Integer distance;

    public SectionRequest() {
    }

    public SectionRequest(String startStation, String endStation, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}
