package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.dao.StationDao;
import subway.domain.Station;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long saveStation(final StationEntity stationEntity) {
        return stationDao.insert(stationEntity);
    }

    public Station findByStationId(final Long stationId) {
        final StationEntity stationEntity = stationDao.findByStationId(stationId)
                .orElseThrow(() -> new IllegalArgumentException("역 식별자값으로 등록된 역이 존재하지 않습니다."));

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Optional<Station> findByStationName(final String stationName) {
        final Optional<StationEntity> maybeStationEntity = stationDao.findByStationName(stationName);

        return maybeStationEntity.map(StationEntity::toDomain);
    }
}
