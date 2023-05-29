package subway.dto;

public final class PathRequest {

    private String startStation;
    private String endStation;

    public PathRequest() {
    }

    public PathRequest(final String startStation, final String endStation) {
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
