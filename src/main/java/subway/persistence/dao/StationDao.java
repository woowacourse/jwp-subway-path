package subway.persistence.dao;

import subway.persistence.entity.StationEntity;

import java.util.List;

public interface StationDao {
    Long createStation(final StationEntity stationEntity);

    List<StationEntity> findAll();

    void deleteById(final Long stationIdRequest);
}
