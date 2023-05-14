package subway.dao;

import subway.domain.Station;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

public interface StationDao {

    StationEntity saveStation(StationEntity stationEntity);

    List<StationEntity> findAll();

    StationEntity findById(Long id);

    Optional<StationEntity> findByName(String name);

    void delete(Long id);
}
