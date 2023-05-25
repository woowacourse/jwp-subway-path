package subway.line.domain.section.application;

import subway.line.domain.station.Station;

import java.util.List;

public class ShortestPathResponse {
    private final Station startingStation;
    private final Station destinationStation;
    private final List<Station> shortestPath;
    private final double shortestDistance;

    public ShortestPathResponse(Station startingStation, Station destinationStation, List<Station> shortestPath, double shortestDistance) {
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.shortestPath = shortestPath;
        this.shortestDistance = shortestDistance;
    }

    public List<Station> getShortestPath() {
        return shortestPath;
    }

    public double getShortestDistance() {
        return shortestDistance;
    }

    public Station getStartingStation() {
        return startingStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }
}
