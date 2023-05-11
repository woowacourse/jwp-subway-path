package subway.dao;

import subway.domain.Station;

import java.util.List;

public interface StationDao {

    Station insert(Station station);

    List<Station> findAll();

    Station findById(Long id);

    void update(Station newStation);

    void deleteById(Long id);

    Station findByName(String name);
}
