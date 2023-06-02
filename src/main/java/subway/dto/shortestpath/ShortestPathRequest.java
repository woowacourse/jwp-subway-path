package subway.dto.shortestpath;

public class ShortestPathRequest {

    private String startStation;
    private String endStation;

    public ShortestPathRequest() {
    }

    public ShortestPathRequest(final String startStation) {
        this.startStation = startStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }
}
