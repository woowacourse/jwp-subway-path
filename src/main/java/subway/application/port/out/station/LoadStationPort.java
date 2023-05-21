package subway.application.port.out.station;

import java.util.List;
import java.util.Optional;
import subway.domain.Station;

public interface LoadStationPort {

    Optional<Station> findById(Long id);

    List<Station> findAll();

    Optional<Station> findByName(String name);
}
