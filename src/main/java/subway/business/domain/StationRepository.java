package subway.business.domain;

import java.util.List;

public interface StationRepository {
    long create(Station station);

    Station findById(Long id);

    List<Station> findAll();

    void update(Station station);

    void deleteById(Long id);
}
