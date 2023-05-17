package subway.domain;

import java.util.List;
import java.util.Optional;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public Station findByName(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        throw new IllegalArgumentException("해당 역은 존재하지 않습니다.");
    }

    public Optional<Station> findNextStationById(Long id) {
        for (Station station : stations) {
            if (station.getId() == id) {
                return Optional.of(station);
            }
        }
        return Optional.empty();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getStationsSize() {
        return stations.size();
    }
}
