package subway.controller.dto.response;

import subway.domain.Distance;
import subway.domain.fee.Fee;
import subway.domain.path.Path;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathResponse {

    private final int distance;
    private final int fee;
    private final List<StationResponse> path;

    public ShortestPathResponse(final int distance, final int fee, final List<StationResponse> path) {
        this.distance = distance;
        this.fee = fee;
        this.path = path;
    }

    public static ShortestPathResponse createByDomain(final Distance distance, final Fee fee, final Path path) {
        final List<StationResponse> stationResponses = path.getStations().stream()
                .map(StationResponse::createByDomain)
                .collect(Collectors.toUnmodifiableList());
        return new ShortestPathResponse(distance.getLength(), fee.getAmount(), stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getPath() {
        return path;
    }
}
