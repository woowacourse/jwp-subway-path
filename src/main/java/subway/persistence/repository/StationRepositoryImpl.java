package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.StationRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;
import subway.domain.Station;

@Repository
public class StationRepositoryImpl implements StationRepository {
    private final StationDao stationDao;

    public StationRepositoryImpl(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long save(Station station) {
        StationEntity stationEntity = new StationEntity(station.getName());

        return stationDao.insert(stationEntity);
    }

    @Override
    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id);

        return new Station(stationEntity.getName());
    }

    @Override
    public Long findIdByName(String name) {
        return stationDao.findIdByName(name);
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public boolean isExistStation(Station station) {
        return stationDao.isExistStationByName(station.getName());
    }
}
