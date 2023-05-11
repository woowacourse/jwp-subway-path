package subway.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class SimpleStationRepository implements StationRepository {

    private Long idIndex;

    final List<Station> stations;

    public SimpleStationRepository() {
        this.stations = new ArrayList<>();
        idIndex = 1L;
    }

    @Override
    public Optional<Station> findById(Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst();
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

    @Override
    public List<Station> findById(List<Long> ids) {
        return ids.stream()
                .map(id -> stations.stream()
                        .filter(station -> station.getId().equals(id)).findFirst().orElseThrow())
                .collect(Collectors.toList());
    }

    @Override
    public List<Station> findAll() {
        return stations;
    }
}
