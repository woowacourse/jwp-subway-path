package subway.domain.station;

import subway.exception.DuplicatedStationNameException;
import subway.exception.StationNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Stations {

    private final Map<Long, Station> stations;

    public Stations() {
        this.stations = new HashMap<>();
    }

    public void add(Station station) {
        validateDuplicatedName(station.getName());
        stations.put(station.getId(), station);
    }

    public void add(Collection<Station> stations) {
        stations.forEach(existStation -> {
            validateDuplicatedName(existStation.getName());
            this.stations.put(existStation.getId(), existStation);
        });
    }

    private void validateDuplicatedName(final String name) {
        if (stations.values().stream().anyMatch(station -> station.isSameName(name))) {
            throw new DuplicatedStationNameException(name);
        }
    }

    public Station get(Long id) {
        validateIsExist(id);
        return stations.get(id);
    }

    private void validateIsExist(final Long id) {
        if (!stations.containsKey(id)) {
            throw new StationNotFoundException();
        }
    }
}
