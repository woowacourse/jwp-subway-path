package subway.domain.station;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }

    public void add(final Station station) {
        validateDuplication(station);
        stations.add(station);
    }

    private void validateDuplication(final Station station) {
        final List<String> stationNames = getStationNames();
        if (stationNames.contains(station.getName())) {
            throw new IllegalArgumentException("역 이름은 중복될 수 없습니다.");
        }
    }

    private List<String> getStationNames() {
        return stations.stream()
            .map(Station::getName)
            .collect(Collectors.toUnmodifiableList());
    }

    public List<Station> getStations() {
        return stations;
    }
}
