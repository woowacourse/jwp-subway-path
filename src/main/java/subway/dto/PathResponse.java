package subway.dto;

import java.util.List;

public class PathResponse {

    private List<Long> shortestPath;
    int fee;

    public PathResponse() {
    }

    public PathResponse(List<Long> shortestPath, int fee) {
        this.shortestPath = shortestPath;
        this.fee = fee;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getFee() {
        return fee;
    }
}
