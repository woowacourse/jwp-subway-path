package subway.persistence.dao;

import subway.domain.station.Station;

import java.util.List;

public interface StationDao {

    Station insert(Station station);

    List<Station> findAll();

    Station findById(Long id);

    void update(Station newStation);

    void deleteById(Long id);

    Station findByName(String name);
}
