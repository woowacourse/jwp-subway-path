package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.repository.dao.StationDao;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(final Station station) {
        return stationDao.insert(station);
    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    @Override
    public Station findById(final Long id) {
        return null;
    }

    @Override
    public void update(final Station newStation) {

    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public Optional<Station> findByName(final String stationName) {
        return Optional.empty();
    }
}
