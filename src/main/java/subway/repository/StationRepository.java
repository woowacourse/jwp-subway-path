package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.dao.v2.StationDaoV2;
import subway.domain.StationDomain;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDaoV2 stationDao;

    public StationRepository(final StationDaoV2 stationDao) {
        this.stationDao = stationDao;
    }

    public Long saveStation(final StationEntity stationEntity) {
        return stationDao.insert(stationEntity);
    }

    public StationDomain findByStationId(final Long stationId) {
        final StationEntity stationEntity = stationDao.findByStationId(stationId)
                .orElseThrow(() -> new IllegalArgumentException("역 식별자값으로 등록된 역이 존재하지 않습니다."));

        return new StationDomain(stationEntity.getId(), stationEntity.getName());
    }

    public Optional<StationDomain> findByStationName(final String stationName) {
        final Optional<StationEntity> maybeStationEntity = stationDao.findByStationName(stationName);

        return Optional.ofNullable(maybeStationEntity.orElseGet(null).toDomain());
    }
}
