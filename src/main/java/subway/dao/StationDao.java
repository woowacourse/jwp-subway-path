package subway.dao;

import subway.entity.StationEntity;

import java.util.List;

public interface StationDao {

    StationEntity insert(StationEntity stationEntity);

    List<StationEntity> findAll();

    StationEntity findById(Long id);

    void update(StationEntity newStationEntity);

    void deleteById(Long id);
}
