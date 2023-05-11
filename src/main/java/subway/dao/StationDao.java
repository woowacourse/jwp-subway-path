package subway.dao;

import subway.domain.Station;

import java.util.List;

public interface StationDao {

    Station saveStation(Station station);

    List<Station> findAll();

    Station findById(Long id);
}
