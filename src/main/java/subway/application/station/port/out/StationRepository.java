package subway.application.station.port.out;

import java.util.List;
import java.util.Optional;
import subway.domain.station.Station;

public interface StationRepository {

    List<Station> findAll();

    Optional<Station> findById(long id);

    Optional<Station> findByName(final String name);

    Station save(final Station station);

    void update(final Station station);

    void deleteById(final long id);
}
