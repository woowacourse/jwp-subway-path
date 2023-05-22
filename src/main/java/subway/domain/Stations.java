package subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Stations {

    private final Set<Station> stations;

    public Stations(Set<Station> stations) {
        this.stations = new HashSet<>(stations);
    }

    public Optional<Station> getStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findAny();
    }

    @Override
    public String toString() {
        return "Stations{" +
                "stations=" + stations +
                '}';
    }

    public List<Station> getStations() {
        return stations.stream()
                .collect(Collectors.toList());
    }
}
