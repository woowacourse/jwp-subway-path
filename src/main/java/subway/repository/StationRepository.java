package subway.repository;

import java.util.List;
import subway.domain.Station;

public interface StationRepository {

    Long insert(final Station station);

    Station findById(final Long id);

    List<Station> findAll();

    void update(final Long id, final Station station);

    void deleteById(final Long id);

    boolean notExistsById(final Long id);
}
