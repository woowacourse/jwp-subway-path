package subway.domain;

import java.util.List;

public interface StationRepository {

    Long save(final Station station);

    void deleteById(final Long id);

    List<Station> findAll();

    Station findById(final Long id);

    Station findByName(final String name);
}
