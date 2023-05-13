package subway.repository;

import subway.domain.Station;

public interface StationRepository {
    Station save(Station station);

    Station findById(Long id);

    Station findByName(Station station);
}
