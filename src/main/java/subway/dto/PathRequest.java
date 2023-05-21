package subway.dto;

public class PathRequest {

    private String startStation;
    private String endStation;

    public PathRequest() {
    }

    public PathRequest(String startStation, String endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }
}
