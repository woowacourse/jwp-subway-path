package subway.dto.route;

import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;

import java.util.List;

public class RouteShortCutResponse {

    private final StationsResponse path;
    private final StationsResponse pathWithLineName;

    public RouteShortCutResponse(final StationsResponse path, final StationsResponse pathWithLineName) {
        this.path = path;
        this.pathWithLineName = pathWithLineName;
    }

    public static RouteShortCutResponse from(final List<StationResponse> shortestPathResponse, final List<StationResponse> shortestPathWithLineResponse) {
        return new RouteShortCutResponse(StationsResponse.from(shortestPathResponse), StationsResponse.from(shortestPathWithLineResponse));
    }

    public StationsResponse getPath() {
        return path;
    }

    public StationsResponse getPathWithLineName() {
        return pathWithLineName;
    }
}
