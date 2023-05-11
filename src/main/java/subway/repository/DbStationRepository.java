package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

@Repository
public class DbStationRepository implements StationRepository {

    private final StationDao stationDao;

    public DbStationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Optional<Station> findById(Long id) {
        Optional<StationEntity> optionalStationEntity = stationDao.findById(id);
        if (optionalStationEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(optionalStationEntity.get().toDomain());
    }

    @Override
    public Optional<Station> findByName(String name) {
        Optional<StationEntity> optionalStationEntity = stationDao.findByName(name);
        if (optionalStationEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(optionalStationEntity.get().toDomain());
    }

    @Override
    public Long create(Station station) {
        return stationDao.insert(StationEntity.from(station));
    }

    @Override
    public List<Station> findById(List<Long> ids) {
        return stationDao.findById(ids).stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }
}
