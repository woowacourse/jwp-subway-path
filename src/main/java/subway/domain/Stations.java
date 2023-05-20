package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
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

    public boolean isExistStation(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int findIndex(Station station) {
        if (stations.contains(station)) {
            return stations.indexOf(station);
        }

        throw new IllegalArgumentException("index를 찾으려고 하는 station이 등록되지 않았습니다");
    }

    public void addStationByIndex(int index, Station station) {
        stations.add(index, station);
    }

    public Station findUpStation(Station station) {
        int index = stations.indexOf(station);
        return stations.get(index - 1);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getStationsSize() {
        return stations.size();
    }
}
