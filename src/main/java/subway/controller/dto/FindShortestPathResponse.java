package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;

public class FindShortestPathResponse {

    private List<StationResponse> stationResponses;
    private Long totalDistance;
    private Long totalCost;

    public FindShortestPathResponse() {
    }

    public FindShortestPathResponse(final List<StationResponse> stationResponses, final Long totalDistance,
        final Long totalCost) {
        this.stationResponses = stationResponses;
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
    }

    public static FindShortestPathResponse of(final Path path, final long totalCost) {
        final List<StationResponse> stationResponses = path.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toUnmodifiableList());
        return new FindShortestPathResponse(stationResponses, path.getDistance().getValue(), totalCost);
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public Long getTotalCost() {
        return totalCost;
    }
}
