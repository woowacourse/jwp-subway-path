package subway.repository;

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

    public Station insert(String stationName) {
        validateDuplicatedStationName(stationName);
        final StationEntity saveStation = stationDao.insert(new StationEntity(stationName));
        return new Station(saveStation.getId(), saveStation.getName());
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

    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }
}
