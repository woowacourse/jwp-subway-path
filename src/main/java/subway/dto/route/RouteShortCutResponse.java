package subway.dto.route;

import subway.dto.fee.FeeResponse;
import subway.dto.station.StationsResponse;

public class RouteShortCutResponse {

    private final StationsResponse stations;
    private final FeeResponse fee;

    private RouteShortCutResponse(final StationsResponse stations, final FeeResponse fee) {
        this.stations = stations;
        this.fee = fee;
    }

    public StationsResponse getStations() {
        return stations;
    }

    public FeeResponse getFee() {
        return fee;
    }
}
