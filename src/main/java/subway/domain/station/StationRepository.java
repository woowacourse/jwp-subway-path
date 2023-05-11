package subway.domain.station;

import java.util.Optional;

public interface StationRepository {
    Optional<Station> findByName(String name);

    Long save(Station station);
}
