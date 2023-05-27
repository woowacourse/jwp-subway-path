package subway.persistence.dao;

import subway.Entity.StationEntity;
import subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

public interface StationDao {

    StationEntity insert(Station station);

    List<StationEntity> findAll();

    Optional<StationEntity> findById(Long id);

    Optional<Long> findIdByName(String name);

    void update(StationEntity stationEntity);

    void deleteById(Long id);
}
