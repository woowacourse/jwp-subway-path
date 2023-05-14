package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.repository.dao.StationDao;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(final Station station) {
        return stationDao.insert(station);
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll();
    }

    @Override
    public Optional<Station> findById(final Long id) {
        final Station station = stationDao.findById(id);
        if (station == null) {
            return Optional.empty();
        }
        return Optional.of(station);
    }

    @Override
    public void update(final Station station) {
        stationDao.update(station);
    }

    @Override
    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public Optional<Station> findByName(final String stationName) {
        final Station station = stationDao.findByName(stationName);
        if (station == null) {
            return Optional.empty();
        }
        return Optional.of(station);
    }
}
