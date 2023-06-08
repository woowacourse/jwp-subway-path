package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.station.Station;

public interface StationRepository {

    Optional<Station> findById(final Long id);

    Optional<Station> findByName(final String name);

    Long create(final Station station);

    List<Station> findById(final List<Long> ids);

    List<Station> findAll();
}
