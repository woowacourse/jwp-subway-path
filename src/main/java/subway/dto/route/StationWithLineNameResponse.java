package subway.dto.route;

import subway.domain.subway.Station;
import subway.dto.station.StationResponse;

import java.util.Set;

public class StationWithLineNameResponse {

    private final StationResponse station;
    private final Set<String> lineNames;

    public StationWithLineNameResponse(final StationResponse station, final Set<String> lineNames) {
        this.station = station;
        this.lineNames = lineNames;
    }

    public static StationWithLineNameResponse from(final Station station, final Set<String> lineName) {
        return new StationWithLineNameResponse(StationResponse.from(station), lineName);
    }

    public StationResponse getStation() {
        return station;
    }

    public Set<String> getLineNames() {
        return lineNames;
    }
}
