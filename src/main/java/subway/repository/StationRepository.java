package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.ErrorCode;
import subway.exception.NoSuchException;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findByName(String name) {
        try {
            StationEntity stationEntity = stationDao.findByName(name);
            return new Station(stationEntity.getId(), stationEntity.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NoSuchException(ErrorCode.NO_SUCH_STATION);
        }
    }
}
