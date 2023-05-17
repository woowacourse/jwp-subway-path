package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final StationEntity stationEntity) {
        return stationDao.insert(stationEntity).toStation();
    }

    public Station findStationById(final Long id) {
        return stationDao.findById(id).toStation();
    }
}
