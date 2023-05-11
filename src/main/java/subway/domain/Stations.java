package subway.domain;

import java.util.HashSet;
import java.util.Set;

public class Stations {

    private final Set<Station> stations;

    public Stations(Set<Station> stations) {
        this.stations = new HashSet<>(stations);
    }

    public boolean contains(Station stationToAdd) {
        return stations.contains(stationToAdd);
    }
}
