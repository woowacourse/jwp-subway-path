package subway.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.persistence.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station insert(final Station station) {
        return stationDao.insert(station);
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public Optional<Station> findById(final Long id) {
        return Optional.ofNullable(stationDao.findById(id));
    }

    public void update(final Station newStation) {
        stationDao.update(newStation);
    }

    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }

    public Optional<Station> findByName(final String name) {
        return Optional.ofNullable(stationDao.findByName(name));
    }
}
