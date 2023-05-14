package subway.repository;

import java.util.List;
import java.util.Optional;
import subway.domain.Station;

public interface StationRepository {

    Station save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);

    void update(Station newStation);

    void deleteById(Long id);

    Optional<Station> findByName(String stationName);
}
