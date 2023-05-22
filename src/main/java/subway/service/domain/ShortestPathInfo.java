package subway.service.domain;

import java.util.Set;

public class ShortestPathInfo {

    private final Integer totalDistance;
    private final Set<LineProperty> usedLines;
    private final Stations stationsInPath;

    public ShortestPathInfo(Integer totalDistance, Set<LineProperty> usedLines, Stations stationsInPath) {
        this.totalDistance = totalDistance;
        this.usedLines = usedLines;
        this.stationsInPath = stationsInPath;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Set<LineProperty> getUsedLines() {
        return usedLines;
    }

    public Stations getStationsInPath() {
        return stationsInPath;
    }

}
