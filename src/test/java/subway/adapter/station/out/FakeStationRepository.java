package subway.adapter.station.out;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.Station;

public class FakeStationRepository implements StationRepository {

    private final Map<Long, Station> stations = new HashMap<>();
    private long id = 1;

    @Override
    public List<Station> findAll() {
        return List.copyOf(stations.values());
    }

    @Override
    public Optional<Station> findById(final long id) {
        return Optional.ofNullable(stations.get(id));
    }

    @Override
    public Optional<Station> findByName(final String name) {
        return stations.values()
            .stream()
            .filter(station -> station.getName().getValue().equals(name))
            .findAny();
    }

    @Override
    public Station save(final Station station) {
        if (station.getId() != null) {
            stations.put(station.getId(), station);
            return station;
        }
        stations.put(id, new Station(id, station.getName().getValue()));
        return stations.get(id++);
    }

    @Override
    public void update(final Station station) {
        stations.get(station.getId()).updateName(station.getName().getValue());
    }

    @Override
    public void deleteById(final long id) {
        stations.remove(id);
    }
}
