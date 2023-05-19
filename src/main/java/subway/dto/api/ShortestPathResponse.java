package subway.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Fare;
import subway.domain.Station;
import subway.dto.domain.StationDto;
import subway.dto.service.PathResult;

@JsonInclude(Include.NON_NULL)
public class ShortestPathResponse {
    private final boolean doesPathExists;
    private StationDto departureStation;
    private StationDto arrivalStation;
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

    public static ShortestPathResponse notFound() {
        return new ShortestPathResponse(null, null, false, null, null, null);
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

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getFare() {
        return fare;
    }

    public List<PathSegmentResponse> getPath() {
        return path;
    }
}
