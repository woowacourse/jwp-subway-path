package subway.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;
import subway.repository.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationEntity save(StationEntity station) {
        return stationDao.insert(station);
    }

    public Optional<StationEntity> findById(Long id) {
        return stationDao.findById(id);
    }

    public StationEntity findOrSaveStation(String stationName) {
        return stationDao.findByName(stationName)
                .orElseGet(() -> stationDao.insert(new StationEntity(stationName)));
    }

    public Optional<StationEntity> findByName(String name) {
        return stationDao.findByName(name);
    }
}
