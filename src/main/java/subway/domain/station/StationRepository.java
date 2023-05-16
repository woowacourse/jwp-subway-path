package subway.domain.station;

import java.util.Optional;

public interface StationRepository {

    Optional<Station> findStationById(Long stationId);
}
