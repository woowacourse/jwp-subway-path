package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.business.domain.Station;
import subway.business.domain.StationRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbStationRepository implements StationRepository {
    private final StationDao stationDao;

    public DbStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public long create(Station station) {
        return stationDao.insert(new StationEntity(station.getName()));
    }

    @Override
    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    @Override
    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Station station) {
        stationDao.update(new StationEntity(station.getId(), station.getName()));
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
