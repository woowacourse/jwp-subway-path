package subway.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.infrastructure.persistence.dao.StationDao;
import subway.infrastructure.persistence.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long save(final Station station) {
        return stationDao.save(StationEntity.from(station));
    }

    @Override
    public Optional<Station> findByName(final String name) {
        return stationDao.findByName(name)
                .map(StationEntity::toDomain);
    }
}
