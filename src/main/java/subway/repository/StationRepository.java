package subway.repository;

import subway.domain.Station;

public interface StationRepository {
    Station save(Station station);

    Station findByName(Long id);

    Station findByName(Station station);
}
