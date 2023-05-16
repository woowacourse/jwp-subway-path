package subway.dto.route;

import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;

import java.util.List;

public class RouteShortCutResponse {

    private final StationsResponse path;
    private final StationsResponse pathWithLineName;
    private final int fee;

    public RouteShortCutResponse(final StationsResponse path, final StationsResponse pathWithLineName, final int fee) {
        this.path = path;
        this.pathWithLineName = pathWithLineName;
        this.fee = fee;
    }

    public static RouteShortCutResponse from(final List<StationResponse> shortestPathResponse,
                                             final List<StationResponse> shortestPathWithLineResponse,
                                             final int fee
    ) {
        return new RouteShortCutResponse(
                StationsResponse.from(shortestPathResponse),
                StationsResponse.from(shortestPathWithLineResponse),
                fee
        );
    }

    public StationsResponse getPath() {
        return path;
    }

    public StationsResponse getPathWithLineName() {
        return pathWithLineName;
    }

    public int getFee() {
        return fee;
    }
}
