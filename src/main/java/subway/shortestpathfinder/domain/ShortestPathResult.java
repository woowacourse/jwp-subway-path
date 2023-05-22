package subway.shortestpathfinder.domain;

import java.util.List;
import java.util.Objects;

public class ShortestPathResult {
    private final List<String> shortestPath;
    private final Long shortestDistance;
    private final Long fee;
    
    public ShortestPathResult(final List<String> shortestPath, final Long shortestDistance, final Long fee) {
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ShortestPathResult that = (ShortestPathResult) o;
        return Objects.equals(shortestPath, that.shortestPath) && Objects.equals(shortestDistance, that.shortestDistance) && Objects.equals(fee, that.fee);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(shortestPath, shortestDistance, fee);
    }
    
    @Override
    public String toString() {
        return "ShortestPathResult{" +
                "shortestPath=" + shortestPath +
                ", shortestDistance=" + shortestDistance +
                ", fee=" + fee +
                '}';
    }
}
