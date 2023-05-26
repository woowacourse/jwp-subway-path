package subway.service.station;

import subway.service.station.domain.Station;

import java.util.List;
import java.util.Set;

public interface StationRepository {
    Station insert(Station station);

    List<Station> findStationsById(Set<Long> ids);

    Station findById(Long id);

    void deleteById(long stationId);
}
