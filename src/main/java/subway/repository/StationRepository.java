package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.station.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station station) {
        final StationEntity stationEntity = StationEntity.from(station);
        return stationDao.insert(stationEntity).toStation();
    }

    public Station findStationById(final Long id) {
        return stationDao.findById(id).toStation();
    }

    public boolean contains(final Station station) {
        return stationDao.findAll().stream()
                .anyMatch(it -> it.getName().equals(station.getName()));
    }
}
