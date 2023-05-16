package subway.domain;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.exception.StationNotFoundException;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findByName(String name) {
        Optional<StationEntity> foundStationEntity = stationDao.findByName(name);
        if (foundStationEntity.isEmpty()) {
            throw new StationNotFoundException();
        }
        return foundStationEntity.get().toDomain();
    }
}
