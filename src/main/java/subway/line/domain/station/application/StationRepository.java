package subway.line.domain.station.application;

import org.springframework.stereotype.Repository;
import subway.line.domain.station.Station;
import subway.line.domain.station.infrastructure.StationDao;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findByName(String name) {
        return stationDao.findByName(name);
    }

    public Station insert(Station station) {
        return stationDao.insert(station);
    }
}
