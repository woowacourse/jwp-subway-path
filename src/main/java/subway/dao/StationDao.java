package subway.dao;

import subway.domain.Station;
import subway.entity.StationEntity;

import java.util.List;

public interface StationDao {

    StationEntity saveStation(StationEntity stationEntity);

    List<StationEntity> findAll();

    StationEntity findById(Long id);

    StationEntity findByName(String name);

    void delete(Long id);
}
