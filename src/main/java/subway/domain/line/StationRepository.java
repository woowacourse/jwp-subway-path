package subway.domain.line;

import java.util.Optional;

public interface StationRepository {

	Station addStation(final String stationName);

	Optional<Station> findById(final Long id);

	Optional<Station> findByNames(final String stationName);

}
