package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;
import subway.exception.NoSuchException;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(Station station) {
        duplicateLineName(station.getName());
        return stationDao.save(new StationEntity(station.getName()));
    }

    private void duplicateLineName(String name) {
        if (stationDao.isExisted(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
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
