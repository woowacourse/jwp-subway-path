package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.ErrorCode;
import subway.exception.NotFoundException;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findById(final Long id) {
        StationEntity stationEntity = stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_STATION));

        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
