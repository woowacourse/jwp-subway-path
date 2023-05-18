package subway.domain;

import subway.exception.StationNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Stations {

    private final Map<Long, Station> stations;

    public Stations() {
        this.stations = new HashMap<>();
    }

    public void add(Station station) {
        stations.put(station.getId(), station);
    }

    public void add(Collection<Station> stations) {
        stations.forEach(this::add);
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

    public Set<Station> toSet() {
        return new HashSet<>(stations.values());
    }
}
