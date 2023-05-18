package subway.dto.api;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Fare;
import subway.domain.Station;
import subway.dto.domain.StationDto;
import subway.dto.service.PathResult;


public class ShortestPathResponse {
    private final StationDto departureStation;
    private final StationDto arrivalStation;

    private final boolean doesPathExists;
    private Integer totalDistance;
    private Integer fare;
    private List<PathSegmentResponse> path;

    public ShortestPathResponse(StationDto departureStation, StationDto arrivalStation, boolean doesPathExists,
                                Integer totalDistance, Integer fare, List<PathSegmentResponse> path) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.doesPathExists = doesPathExists;
        this.totalDistance = totalDistance;
        this.fare = fare;
        this.path = path;
    }

    public static ShortestPathResponse of(Station departure, Station arrival, PathResult pathResult, Fare fare) {
        return new ShortestPathResponse(
                StationDto.from(departure),
                StationDto.from(arrival),
                true,
                pathResult.getPath().calculateTotalDistance().getValue(),
                fare.getValue(),
                pathResult.getLineToStations().entrySet().stream()
                        .map(entry -> PathSegmentResponse.of(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );
    }

    public StationDto getDepartureStation() {
        return departureStation;
    }

    public StationDto getArrivalStation() {
        return arrivalStation;
    }

    public boolean isDoesPathExists() {
        return doesPathExists;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }

    public List<PathSegmentResponse> getPath() {
        return path;
    }
}
