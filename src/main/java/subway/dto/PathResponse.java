package subway.dto;

import java.util.List;

public final class PathResponse {

    private List<StationResponse> stationResponses;
    private int distance;
    private int cost;

    public PathResponse(List<StationResponse> stationResponses, int distance, int cost) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.cost = cost;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost() {
        return cost;
    }
}
