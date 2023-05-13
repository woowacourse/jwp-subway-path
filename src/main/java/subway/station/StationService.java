package subway.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.persistence.StationDao;
import subway.station.persistence.StationEntity;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long create(final Station station) {
        return stationDao.insert(station);
    }

    public StationEntity findByName(final String name) {
        return stationDao.findByName(name);
    }
}
