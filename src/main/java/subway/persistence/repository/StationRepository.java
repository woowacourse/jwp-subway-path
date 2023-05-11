package subway.persistence.repository;

import java.util.Optional;
import subway.domain.Station;

public interface StationRepository {

    Long save(final Station station);

    Optional<Station> findByName(final String name);
}
