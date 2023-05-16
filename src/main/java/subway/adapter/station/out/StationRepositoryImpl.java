package subway.adapter.station.out;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.application.station.port.out.StationRepository;
import subway.domain.station.Station;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        stationDao = new StationDao(jdbcTemplate);
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll()
            .stream()
            .map(StationEntity::toStation)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Station> findById(final long id) {
        return stationDao.findById(id)
            .map(StationEntity::toStation);
    }

    @Override
    public Optional<Station> findByName(final String name) {
        return stationDao.findByName(name)
            .map(StationEntity::toStation);
    }

    @Override
    public Station save(final Station station) {
        return stationDao.insert(StationEntity.from(station))
            .toStation();
    }

    @Override
    public void update(final Station station) {
        stationDao.update(StationEntity.from(station));
    }

    @Override
    public void deleteById(final long id) {
        stationDao.deleteById(id);
    }
}
