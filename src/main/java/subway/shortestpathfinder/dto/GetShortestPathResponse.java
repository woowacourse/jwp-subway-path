package subway.shortestpathfinder.dto;

import subway.shortestpathfinder.domain.ShortestPathResult;

import java.util.List;

public class GetShortestPathResponse {
    private final List<String> shortestPath;
    private final Long shortestDistance;
    private final Long fee;
    
    public GetShortestPathResponse(final ShortestPathResult shortestPathResult) {
        this(shortestPathResult.getShortestPath(), shortestPathResult.getShortestDistance(), shortestPathResult.getFee());
    }
    
    public GetShortestPathResponse(final List<String> shortestPath, final Long shortestDistance, final Long fee) {
        this.shortestPath = shortestPath;
        this.shortestDistance = shortestDistance;
        this.fee = fee;
    }
    
    public List<String> getShortestPath() {
        return shortestPath;
    }
    
    public Long getShortestDistance() {
        return shortestDistance;
    }
    
    public Long getFee() {
        return fee;
    }
    
    @Override
    public String toString() {
        return "GetShortestPathResponse{" +
                "shortestPath=" + shortestPath +
                ", shortestDistance=" + shortestDistance +
                ", fee=" + fee +
                '}';
    }
}
