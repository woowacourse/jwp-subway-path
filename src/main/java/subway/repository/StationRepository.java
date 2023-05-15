package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station station) {
        final Optional<Station> stationOptional = stationDao.findByName(station.getName());

        return stationOptional.orElseGet(() -> stationDao.insert(station.getName()));
    }

    public Optional<Station> findById(final Long stationId) {

        return stationDao.findById(stationId);
    }
}
