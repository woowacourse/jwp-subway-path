package subway.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import subway.domain.Station;
import subway.repository.StationRepository;

public class StationRepositoryFake implements StationRepository {

    private static final Map<Long, Station> store = new HashMap<>();

    @Override
    public Station insert(final Station station) {
        store.put(station.getId(), station);
        return station;
    }

    @Override
    public List<Station> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Station findById(final Long id) {
        Station station = store.get(id);
        if (station == null) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
        return station;
    }

    @Override
    public void update(final Station station) {
        store.put(station.getId(), station);
    }

    @Override
    public void deleteById(final Long id) {
        store.remove(id);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
