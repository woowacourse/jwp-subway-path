package subway.domain.repository;

import subway.domain.Station;

import java.util.Optional;

public interface StationRepository {
    Long createStation(final Station station);

    Optional<Station> findByName(Station station);
}
