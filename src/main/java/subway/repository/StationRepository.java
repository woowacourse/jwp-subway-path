package subway.repository;

import java.util.Optional;
import subway.domain.Station;

public interface StationRepository {

    Optional<Station> findById(final Long id);

    Optional<Station> findByName(final String name);

    Long create(final Station station);
}
