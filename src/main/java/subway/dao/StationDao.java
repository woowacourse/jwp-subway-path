package subway.dao;

import java.util.List;

import subway.domain.Station;

public interface StationDao {

    Station insert(final Station station);

    List<Station> findAll();

    Station findById(final Long id);

    void update(final Station station);

    void deleteById(final Long id);
}
