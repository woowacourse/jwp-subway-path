package subway.dto;

public class LineRequest {
    private String lineName;

    private String startStation;
    private String endStation;
    private Integer distance;

    public LineRequest() {
    }

    public LineRequest(String lineName, String startStation, String endStation, Integer distance) {
        this.lineName = lineName;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
