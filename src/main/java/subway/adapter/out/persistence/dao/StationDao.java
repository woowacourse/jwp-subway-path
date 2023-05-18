package subway.adapter.out.persistence.dao;

import subway.adapter.out.persistence.entity.StationEntity;

import java.util.List;

public interface StationDao {
    Long createStation(final StationEntity stationEntity);

    List<StationEntity> findAll();

    void deleteById(final Long stationIdRequest);
}
