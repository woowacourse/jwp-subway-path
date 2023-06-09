package subway.station.domain;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    List<Station> findAll();

    Optional<Station> findById(long id);

    Optional<Station> findByName(String name);

    Station save(Station station);

    void update(Station station);

    void deleteById(long id);
}
