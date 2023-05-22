package subway.line.domain.station.application;

import org.springframework.stereotype.Repository;
import subway.line.domain.station.Station;
import subway.line.domain.station.infrastructure.StationDao;

import java.util.List;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station insert(String name) {
        return stationDao.insert(name);
    }

    public Station findById(Long id) {
        return stationDao.findById(id);
    }

    public List<Station> findAll() {
        return stationDao.findAll();
    }

    public void update(Station station) {
        stationDao.update(station);
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
