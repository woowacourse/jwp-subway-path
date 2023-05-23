package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class StationNamesConvertor {

    private StationNamesConvertor() {
    }

    public static List<String> convertToStationNamesByStations(final List<Station> shortestPath) {
        return shortestPath.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}
