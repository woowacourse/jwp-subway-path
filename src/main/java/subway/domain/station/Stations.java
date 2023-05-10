package subway.domain.station;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.section.Sections;

public class Stations {

    private static final int INVALID_STATION_COUNT = 1;

    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        validateStationCount(stations);
        validateStationDuplicateName(stations);
        this.stations = stations;
    }

    private void validateStationCount(final List<Station> stations) {
        if (stations.size() == INVALID_STATION_COUNT) {
            throw new IllegalArgumentException("노선의 역은 1개만 존재할 수 없습니다.");
        }
    }

    private void validateStationDuplicateName(final List<Station> stations) {
        List<String> names = stations.stream()
            .map(Station::getName)
            .collect(Collectors.toList());

        if (names.size() != new HashSet<>(names).size()) {
            throw new IllegalArgumentException("역 이름은 중복될 수 없습니다.");
        }
    }

    public int size() {
        return stations.size();
    }

    public boolean isCorrectSectionsSize(final Sections sections) {
        return stations.size() - 1 == sections.size();
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public Station getFirstStation() {
        return stations.get(0);
    }

    public Station getLastStation() {
        return stations.get(size());
    }
}
