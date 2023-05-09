package subway.repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class SimpleStationRepository implements StationRepository {

    private Long idIndex;

    final Set<Station> stations;

    public SimpleStationRepository() {
        this.stations = new HashSet<>();
        idIndex = 1L;
    }

    @Override
    public Optional<Station> findByName(String name) {
        return stations.stream()
                .filter(station -> station.getName().equals(name))
                .findFirst();
    }

    @Override
    public Long create(Station station) {
        stations.add(new Station(idIndex, station.getName()));
        return idIndex++;
    }
}
