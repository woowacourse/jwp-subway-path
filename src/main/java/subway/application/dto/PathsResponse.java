package subway.application.dto;

import java.util.List;
import java.util.Objects;

public class PathsResponse {

    private List<StationResponse> paths;
    private Integer distance;
    private Integer cost;

    private PathsResponse(List<StationResponse> paths, Integer distance, Integer cost) {
        this.paths = paths;
        this.distance = distance;
        this.cost = cost;
    }

    public static PathsResponse of(List<StationResponse> paths, Integer distance, Integer cost) {
        return new PathsResponse(paths, distance, cost);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathsResponse that = (PathsResponse) o;
        return Objects.equals(paths, that.paths) && Objects.equals(distance, that.distance) && Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paths, distance, cost);
    }

    @Override
    public String toString() {
        return "PathsResponse{" +
                "paths=" + paths +
                ", distance=" + distance +
                ", cost=" + cost +
                '}';
    }
}
