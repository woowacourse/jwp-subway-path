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

    public Optional<Station> findById(Long stationId) {
        return stationDao.findById(stationId)
                .map(StationEntity::toDomain);
    }

    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }
}
