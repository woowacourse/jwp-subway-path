package subway.dto.route;

import subway.domain.subway.Station;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShortestPathResponse {

    private final List<StationWithLineNameResponse> stations;
    private final int fee;

    public ShortestPathResponse(final List<StationWithLineNameResponse> stations, final int fee) {
        this.stations = stations;
        this.fee = fee;
    }

    public static ShortestPathResponse from(final Map<Station, Set<String>> map, final int fee) {
        List<StationWithLineNameResponse> result = map.keySet().stream()
                .map(key -> StationWithLineNameResponse.from(key, map.get(key)))
                .collect(Collectors.toList());

        return new ShortestPathResponse(result, fee);
    }

    public List<StationWithLineNameResponse> getStations() {
        return stations;
    }

    public int getFee() {
        return fee;
    }
}
