package subway.application.dto;

import java.util.List;

public class PathsResponse {

    private List<StationResponse> paths;
    private Integer distance;
    private Integer cost;

    public PathsResponse(List<StationResponse> paths, Integer distance, Integer cost) {
        this.paths = paths;
        this.distance = distance;
        this.cost = cost;
    }

    public List<StationResponse> getPaths() {
        return paths;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getCost() {
        return cost;
    }
}
