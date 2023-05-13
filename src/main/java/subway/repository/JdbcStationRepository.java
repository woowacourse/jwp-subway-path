package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.exception.NoSuchStationException;
import subway.mapper.StationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station findById(final Long id) {
        StationEntity entity = stationDao.findById(id)
                .orElseThrow(() -> new NoSuchStationException(id));
        return StationMapper.toStation(entity);
    }

    @Override
    public Station insert(Station station) {
        StationEntity entity = StationMapper.toEntity(station);
        StationEntity saved = stationDao.insert(entity);
        return StationMapper.toStation(saved);
    }

    @Override
    public void update(Long id, Station station) {
        StationEntity stationEntity = new StationEntity(id, station.getName());
        stationDao.update(stationEntity);
    }

    @Override
    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(StationMapper::toStation)
                .collect(Collectors.toList());
    }
}
