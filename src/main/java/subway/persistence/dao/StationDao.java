package subway.persistence.dao;

import subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

public interface StationDao {

    Station insert(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    void update(Station newStation);

    void deleteById(Long id);
}
