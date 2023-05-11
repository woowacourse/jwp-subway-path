package subway.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.mapper.StationMapper;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Optional<Station> findById(final Long id) {
        Optional<StationEntity> entity = stationDao.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(StationMapper.toStation(entity.get()));
    }
}
