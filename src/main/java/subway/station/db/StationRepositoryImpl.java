package subway.station.db;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.domain.exception.StationAlreadyRegisteredException;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(JdbcTemplate jdbcTemplate) {
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
    public Optional<Station> findById(long id) {
        return stationDao.findById(id)
                .map(StationEntity::toStation);
    }

    @Override
    public Optional<Station> findByName(String name) {
        return stationDao.findByName(name)
                .map(StationEntity::toStation);
    }

    @Override
    public Station save(Station station) {
        try {
            return stationDao.insert(StationEntity.from(station))
                    .toStation();
        } catch (DuplicateKeyException e) {
            throw new StationAlreadyRegisteredException();
        }
    }

    @Override
    public void update(Station station) {
        stationDao.update(StationEntity.from(station));
    }

    @Override
    public void deleteById(long id) {
        stationDao.deleteById(id);
    }
}
