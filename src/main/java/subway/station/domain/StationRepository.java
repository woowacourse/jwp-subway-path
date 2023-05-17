package subway.station.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository {

  Station findById(final Long id);

  Station createStation(final String name);
}
