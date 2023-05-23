package subway.controller.dto.response;

import subway.service.domain.ShortestPath;
import subway.service.domain.Station;

public class ShortestPathResponse {

    private final Station sourceStation;
    private final Station destinationStation;
    private final Integer totalDistance;
    private final Integer fare;

    public ShortestPathResponse(Station sourceStation, Station destinationStation, Integer totalDistance, Integer fare) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public static ShortestPathResponse of(Station source,
                                          Station destination,
                                          ShortestPath shortestPath) {
        return new ShortestPathResponse(
                source,
                destination,
                shortestPath.getShortestPathInfo().getTotalDistance(),
                shortestPath.getFare().getValue()
        );
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getFare() {
        return fare;
    }

}
