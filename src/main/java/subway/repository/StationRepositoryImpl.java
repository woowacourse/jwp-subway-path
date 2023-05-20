package subway.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private static final HashMap<Long, Station> store = new HashMap<>();

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station insert(Station station) {
        Station storedStation = stationDao.insert(station);
        store.put(storedStation.getId(), storedStation);
        return storedStation;
    }

    @Override
    public List<Station> findAll() {
        init();
        return new ArrayList<>(store.values());
    }

    private void init() {
        if (store.isEmpty()) {
            List<Station> stations = stationDao.findAll();
            for (Station station : stations) {
                store.put(station.getId(), station);
            }
        }
    }

    @Override
    public Station findById(Long id) {
        init();
        return store.get(id);
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
}
