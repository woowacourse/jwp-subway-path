package subway.domain;

import java.util.Optional;

public interface StationRepository {

    void save(final Station station);

    Optional<Station> findByName(final String name);
}
