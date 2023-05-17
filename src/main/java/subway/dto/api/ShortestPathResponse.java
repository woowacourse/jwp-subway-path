package subway.dto.api;

import java.util.List;
import subway.dto.domain.LineDto;
import subway.dto.domain.StationDto;


public class ShortestPathResponse {
    private final StationDto departureStation;
    private final StationDto arrivalStation;

    private final boolean doesPathExists;
    private Integer totalDistance;
    private Integer fare;
    private List<PathResponse> path;

    public static ShortestPathResponse pathNotExistBetween(StationDto deperture, StationDto arrival) {
        return new ShortestPathResponse(deperture, arrival, false, null, null, null);
    }

    public ShortestPathResponse(StationDto departureStation, StationDto arrivalStation, boolean doesPathExists,
                                Integer totalDistance, Integer fare, List<PathResponse> path) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.doesPathExists = doesPathExists;
        this.totalDistance = totalDistance;
        this.fare = fare;
        this.path = path;
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

    public List<PathResponse> getPath() {
        return path;
    }
}
