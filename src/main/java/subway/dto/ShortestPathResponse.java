package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private List<String> shortestPath;
    int fee;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(List<String> shortestPath, int fee) {
        this.shortestPath = shortestPath;
        this.fee = fee;
    }

    public List<String> getShortestPath() {
        return shortestPath;
    }

    public int getFee() {
        return fee;
    }
}
