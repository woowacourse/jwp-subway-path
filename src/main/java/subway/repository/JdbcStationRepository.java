package subway.repository;

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
    public Station findById(final Long id) {
        StationEntity entity = stationDao.findById(id);
        return StationMapper.toStation(entity);
    }
}
