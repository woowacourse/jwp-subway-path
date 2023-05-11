package subway.dao;

import subway.domain.Station;

import java.util.List;

public interface StationDao {

    Station saveStation(Station station);

    List<Number> saveStations(List<Station> stations);
}
