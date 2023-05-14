package subway.domain.station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public Optional<Station> findStationByStationName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findFirst();
    }
}
