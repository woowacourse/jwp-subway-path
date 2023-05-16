package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findById(final Long id) {
        Optional<StationEntity> maybeStationEntity = stationDao.findById(id);
        if (maybeStationEntity.isEmpty()) {
            return Station.EMPTY_STATION;
        }
        return maybeStationEntity.get().convertToStation();
    }

    public Long save(final Station station) {
        StationEntity stationEntity = StationEntity.from(station);
        return stationDao.save(stationEntity);
    }
}
