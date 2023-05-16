package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.exception.InvalidStationException;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findById(final Long stationId) {
        final StationEntity entity = stationDao.findById(stationId)
                .orElseThrow(() -> new InvalidStationException("존재하지 않은 역 ID입니다."));
        return generateStation(entity);
    }

    public Station save(final Station station) {
        final StationEntity entity = stationDao.save(new StationEntity(station.getName()));
        return generateStation(entity);
    }

    private Station generateStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
