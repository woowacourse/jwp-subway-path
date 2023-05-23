package subway.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import subway.domain.station.Station;

public class StubStationDao implements StationDao {

    private final Map<Long, Station> stationMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public Station insert(final Station station) {
        final long currentId = maxId.incrementAndGet();
        final Station saved = new Station(currentId, station.getName());
        stationMap.put(currentId, saved);
        return saved;
    }

    @Override
    public List<Station> findAll() {
        return new ArrayList<>(stationMap.values());
    }

    @Override
    public Station findById(final Long id) {
        return stationMap.get(id);
    }

    @Override
    public void update(final Station station) {
        stationMap.put(station.getId(), station);
    }

    @Override
    public void deleteById(final Long id) {
        stationMap.remove(id);
    }
}
