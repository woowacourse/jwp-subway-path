package subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.line.Station;
import subway.exception.line.StationAlreadyExistException;
import subway.exception.line.StationDoesNotExistException;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station getStation(Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new StationDoesNotExistException());
    }

    public Station getStation(String stationName) {
        return stationDao.findByName(stationName)
                .orElseThrow(() -> new StationDoesNotExistException());
    }

    public void checkStationIsExist(String stationName) {
        stationDao.findByName(stationName)
                .ifPresent(station -> {
                    throw new StationAlreadyExistException();
                });
    }

    public Station insert(Station station) {
        return stationDao.insert(station);
    }
}
