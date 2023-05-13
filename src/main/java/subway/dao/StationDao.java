package subway.dao;

import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

public interface StationDao {

    Optional<StationEntity> findById(Long id);

    List<StationEntity> findAll();

    StationEntity insert(StationEntity stationEntity);

    void update(StationEntity newStationEntity);

    void deleteById(Long id);
}
