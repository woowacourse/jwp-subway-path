package subway.dao;

import subway.domain.Station;

import java.util.List;
import java.util.Optional;

public interface StationDao {

    Station insert(final Station station);

    List<Station> findAll();

    Optional<Station> findById(final long id);

    void update(final Station newStation);

    void deleteById(final long id);

    Station findByName(final String name);

    int countByName(final String name);
}
