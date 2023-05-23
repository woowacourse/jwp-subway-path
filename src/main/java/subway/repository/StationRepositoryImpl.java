package subway.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private static final Map<Long, Station> store = new HashMap<>();

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
        init();
    }

    @Override
    public Station insert(Station station) {
        validateDuplicate(station);
        Station storedStation = stationDao.insert(station);
        store.put(storedStation.getId(), storedStation);
        return storedStation;
    }

    private void validateDuplicate(final Station station) {
        boolean isPresent = store.values().stream()
                .filter(iter -> iter.getName().equals(station.getName()))
                .findAny()
                .isPresent();
        if (isPresent) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }

    @Override
    public List<Station> findAll() {
        return new ArrayList<>(store.values());
    }

    private void init() {
        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            store.put(station.getId(), station);
        }
    }

    @Override
    public Station findById(Long id) {
        Station station = store.get(id);
        if (station == null) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
        return station;
    }

    @Override
    public void update(Station station) {
        stationDao.update(station);
        store.put(station.getId(), station);
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
        store.remove(id);
    }

    @Override
    public void deleteAll() {
        stationDao.deleteAll();
        store.clear();
    }
}
