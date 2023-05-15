package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> generateSortedStations(List<Long> orderedStationIds) {
        Map<Long, Station> idToStations = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        return orderedStationIds.stream()
                .map(idToStations::get)
                .collect(Collectors.toList());
    }
}
