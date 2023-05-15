package subway.domain.station;

import java.util.Optional;

public interface StationRepository {
    Optional<Station> findByName(String name);

    Station saveIfNotExist(Station station);

    Optional<Station> findById(Long stationId);
}
