package subway.service.dto;

public class ShortestRouteRequest {

    private String startStation;
    private String endStation;

    public ShortestRouteRequest(final String startStation, final String endStation) {
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
