package subway.domain.station;

import java.util.Optional;

public interface StationRepository {
    Station saveIfNotExist(Station station);

    Optional<Station> findById(Long stationId);
}
