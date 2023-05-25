package subway.line.presentation.dto;

import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.station.presentation.dto.StationResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class SectionResponse {
    private final StationResponse startingStation;
    private final StationResponse destinationStation;
    private final List<StationResponse> shortestStationPath;
    private final double shortestDistance;
    private final BigDecimal fare;

    public SectionResponse(StationResponse startingStation, StationResponse destinationStation, List<StationResponse> shortestStationPath, double shortestDistance, BigDecimal fare) {
        this.startingStation = startingStation;
        this.destinationStation = destinationStation;
        this.shortestStationPath = shortestStationPath;
        this.shortestDistance = shortestDistance;
        this.fare = fare;
    }

    public static SectionResponse of(ShortestPathResponse shortestPath, BigDecimal fare) {
        return new SectionResponse(
                StationResponse.of(shortestPath.getStartingStation()),
                StationResponse.of(shortestPath.getDestinationStation()),
                shortestPath.getShortestPath()
                        .stream().map(StationResponse::of)
                        .collect(Collectors.toList()),
                shortestPath.getShortestDistance(),
                fare
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

    public double getShortestDistance() {
        return shortestDistance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
