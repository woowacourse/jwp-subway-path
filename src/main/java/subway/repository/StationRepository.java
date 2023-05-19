package subway.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.exception.DuplicatedNameException;
import subway.repository.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationEntity save(String stationName) {
        validateDuplicatedStationName(stationName);
        return stationDao.insert(new StationEntity(stationName));
    }

    private void validateDuplicatedStationName(final String stationName) {
        if (stationDao.existsByName(stationName)) {
            throw new DuplicatedNameException(stationName);
        }
    }

    public Station findById(Long stationId) {
        final StationEntity findStationEntity = stationDao.findById(stationId);
        final Long id = findStationEntity.getId();
        final String name = findStationEntity.getName();
        return new Station(id, name);
    }

    public StationEntity findOrSaveStation(String stationName) {
        return stationDao.findByName(stationName)
                .orElseGet(() -> stationDao.insert(new StationEntity(stationName)));
    }

    public Optional<StationEntity> findByName(String name) {
        return stationDao.findByName(name);
    }

    public void delete(final Long stationId) {
        stationDao.deleteById(stationId);
    }
}
