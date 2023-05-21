package subway.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

@Repository
public class JdbcStationRepository implements StationRepository {

    private static final String NO_SUCH_STATION_MESSAGE = "해당하는 역이 존재하지 않습니다.";

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long insert(final Station station) {
        return stationDao.insert(StationEntity.from(station));
    }

    @Override
    public Station findById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION_MESSAGE))
                .toDomain();
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void update(final Long id, final Station station) {
        stationDao.update(id, StationEntity.from(station));
    }

    @Override
    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public boolean notExistsById(final Long id) {
        return stationDao.notExistsById(id);
    }
}
