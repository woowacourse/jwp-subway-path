package subway.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;

public class FindShortestPathResponse {

    private List<StationInformationResponse> stationInformations;
    private Long totalDistance;
    private Long totalCost;

    public FindShortestPathResponse() {
    }

    public FindShortestPathResponse(final List<StationInformationResponse> stationInformations,
        final Long totalDistance,
        final Long totalCost) {
        this.stationInformations = stationInformations;
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
    }

    public static FindShortestPathResponse of(final Path path, final long totalCost) {
        final List<StationInformationResponse> stationInformationResponses = path.getStationInformations()
            .stream()
            .map(StationInformationResponse::of)
            .collect(Collectors.toUnmodifiableList());
        return new FindShortestPathResponse(stationInformationResponses, path.getDistance().getValue(), totalCost);
    }

    public List<StationInformationResponse> getStationInformations() {
        return stationInformations;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public Long getTotalCost() {
        return totalCost;
    }
}
