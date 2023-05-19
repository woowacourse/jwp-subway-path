package subway.dao;

import subway.entity.StationEntity;

import java.util.Optional;

public interface StationDao {
    StationEntity insert(StationEntity stationEntity);

    Optional<StationEntity> findBy(Long id);
}
