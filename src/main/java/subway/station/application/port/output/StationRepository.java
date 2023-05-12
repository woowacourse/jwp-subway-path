package subway.station.application.port.output;

import subway.station.domain.Station;

import java.util.Optional;

public interface StationRepository {
    Optional<Station> findByName(String name);

    Long save(Station station);
}
