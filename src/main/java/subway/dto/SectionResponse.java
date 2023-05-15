package subway.dto;

import java.util.List;

public class SectionResponse {
    private final StationResponse startingStation;
    private final StationResponse destinationStation;
    private final List<StationResponse> shortestStationPath;
    private final int distance;

    public SectionResponse(StationResponse startingStation, StationResponse destinationStation, List<StationResponse> shortestStationPath, int distance) {
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.shortestStationPath = shortestStationPath;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }
}
