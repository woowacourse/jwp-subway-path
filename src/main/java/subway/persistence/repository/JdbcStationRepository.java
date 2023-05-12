package subway.persistence.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

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
