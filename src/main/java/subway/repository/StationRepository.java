package subway.repository;

import java.util.Optional;
import subway.domain.Station;

public interface StationRepository {

    Optional<Station> findById(Long id);
}
