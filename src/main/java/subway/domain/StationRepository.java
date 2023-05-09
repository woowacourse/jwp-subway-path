package subway.domain;

import java.util.Optional;

public interface StationRepository {

    Long save(final Station station);

    Optional<Station> findById(final Long id);
}
