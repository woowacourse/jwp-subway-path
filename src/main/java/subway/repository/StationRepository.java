package subway.repository;

import java.util.List;
import subway.domain.Station;

public interface StationRepository {

    Station insert(Station station);

    List<Station> findAll();

    Station findById(Long id);

    void update(Station station);

    void deleteById(Long id);
}
