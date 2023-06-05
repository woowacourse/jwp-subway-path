package subway.station.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

public class FakeStationRepository implements StationRepository {

    private final Map<Long, Station> stations = new HashMap<>();
    private long id = 1;

    @Override
    public List<Station> findAll() {
        return List.copyOf(stations.values());
    }

    @Override
    public Optional<Station> findById(long id) {
        return Optional.ofNullable(stations.get(id));
    }

    @Override
    public Optional<Station> findByName(String name) {
        return stations.values()
                .stream()
                .filter(station -> station.getName().getValue().equals(name))
                .findAny();
    }

    @Override
    public Station save(Station station) {
        if (station.getId() != null) {
            stations.put(station.getId(), station);
            return station;
        }
        stations.put(id, new Station(id, station.getName().getValue()));
        return stations.get(id++);
    }

    @Override
    public void update(Station station) {
        stations.get(station.getId()).updateName(station.getName().getValue());
    }

    @Override
    public void deleteById(long id) {
        stations.remove(id);
    }
}
