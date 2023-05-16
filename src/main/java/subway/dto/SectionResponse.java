package subway.dto;

import subway.application.dto.ShortestPathResponse;

import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private final StationResponse startingStation;
    private final StationResponse destinationStation;
    private final List<StationResponse> shortestStationPath;
    private final double distance;

    public SectionResponse(StationResponse startingStation, StationResponse destinationStation, List<StationResponse> shortestStationPath, double distance) {
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.shortestStationPath = shortestStationPath;
        this.distance = distance;
    }

    public static SectionResponse of(ShortestPathResponse shortestPath) {
        return new SectionResponse(
                StationResponse.of(shortestPath.getStartingStation()),
                StationResponse.of(shortestPath.getDestinationStation()),
                shortestPath.getShortestPath()
                        .stream().map(StationResponse::of)
                        .collect(Collectors.toList()),
                shortestPath.getShortestDistance()
        );
    }

    public StationResponse getStartingStation() {
        return startingStation;
    }

    public StationResponse getDestinationStation() {
        return destinationStation;
    }

    public List<StationResponse> getShortestStationPath() {
        return shortestStationPath;
    }

    public double getDistance() {
        return distance;
    }
}
