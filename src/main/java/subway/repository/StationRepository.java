package subway.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.repository.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        return stationDao.insert(station);
    }

    public Optional<Station> findById(Long id) {
        return stationDao.findById(id);
    }

    public Station findOrSaveStation(String stationName) {
        return stationDao.findByName(stationName)
                .orElseGet(() -> stationDao.insert(new Station(stationName)));
    }

    public Optional<Station> findByName(String name) {
        return stationDao.findByName(name);
    }
}
