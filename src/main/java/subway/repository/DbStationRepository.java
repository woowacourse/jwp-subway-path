package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

@Repository
public class DbStationRepository implements StationRepository {
    private final StationDao stationDao;

    public DbStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(final Station station) {
        final StationEntity stationEntity = new StationEntity(station.getName());
        return Station.from(stationDao.insert(stationEntity));
    }

    @Override
    public Station findById(final Long id) {
        return Station.from(stationDao.findBy(id));
    }

    @Override
    public Station findByName(final Station station) {
        return Station.from(stationDao.findBy(station.getName()));
    }
}
