package subway.line.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;
import subway.line.infrastructure.persistence.dao.StationDao;
import subway.line.infrastructure.persistence.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public void save(final Station station) {
        stationDao.save(StationEntity.from(station));
    }

    @Override
    public Optional<Station> findByName(final String name) {
        return stationDao.findByName(name)
                .map(StationEntity::toDomain);
    }
}
