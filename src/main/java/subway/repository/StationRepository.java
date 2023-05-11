package subway.repository;

import java.util.Optional;
import subway.domain.Station;

public interface StationRepository {

    Station save(final Station station);

    Optional<Station> findByName(String name);

    void delete(Station existStation);
}
