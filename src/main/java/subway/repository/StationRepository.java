package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }


    public Station insert(Station station) {
        return stationDao.insert(station);
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public Optional<Station> findById(Long id) {
        return stationDao.findById(id);
    }

    public void update(Station newStation) {
        stationDao.update(newStation);
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
