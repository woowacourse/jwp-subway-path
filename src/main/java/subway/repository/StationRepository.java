package subway.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    @Autowired
    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }


    public Optional<Station> findStationByName(String stationName) {
        return Optional.empty();
    }

    public Optional<Station> findStationById(long id) {
        return Optional.empty();
    }

    public long insert(Station stationName) {
        return 0L;
    }

    public Optional<Long> findIdByName(String stationName) {
        return Optional.empty();
    }
}
