package subway.application.port.out.station;

import subway.domain.Station;

import java.util.Optional;

public interface StationQueryPort {
    Optional<Station> findByName(Station station);
}
