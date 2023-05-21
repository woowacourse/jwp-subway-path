package subway.domain;

import java.util.Optional;

public interface StationRepository {

	Station addStation(final String stationName);

	Optional<Station> findByStationNames(final String stationName);

}
