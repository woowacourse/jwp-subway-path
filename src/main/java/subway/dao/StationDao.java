package subway.dao;

import subway.entity.StationEntity;

public interface StationDao {
    StationEntity insert(StationEntity stationEntity);

    StationEntity findBy(Long id);

    StationEntity findBy(String name);
}
