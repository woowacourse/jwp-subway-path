package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public List<Station> addStationInLine(Station station, Station nextStation, Distance distance) {
        Station existStation = isExistStation(station.getName()) ? station : nextStation;
        Station newStation = isExistStation(nextStation.getName()) ? nextStation : station;

        setDistances(existStation, newStation, station, distance);
        int index = findInputIndex(existStation, nextStation);
        stations.add(index, newStation);

        return stations;
    }

    private void setDistances(Station existStation, Station newStation, Station station, Distance distance) {
        int existStationIndex = findIndex(existStation);

        if (existStationIndex == 0) {
            newStation.setDistance(distance);
        } else if (existStationIndex == getStationsSize() - 1) {
            existStation.setDistance(distance);
        } else {
            setDistanceInCaseMiddleAdd(existStation, newStation, station, distance, existStationIndex);
        }
    }

    private void setDistanceInCaseMiddleAdd(Station existStation, Station newStation, Station station, Distance distance, int existStationIndex) {
        if (existStation.equals(station)) {
            Distance originalDistance = existStation.getDistance();
            newStation.setDistance(originalDistance.abs(distance));
            existStation.setDistance(distance);
        } else {
            Station upStation = stations.get(existStationIndex - 1);
            Distance originalDistance = upStation.getDistance();
            upStation.setDistance(distance);
            newStation.setDistance(originalDistance.abs(distance));
        }
    }

    private int findInputIndex(Station existStation, Station nextStation) {
        int existStationIndex = findIndex(existStation);

        if (existStationIndex == getStationsSize() - 1) {
            return existStationIndex + 1;
        } else {
            if (existStation.equals(nextStation)) {
                return existStationIndex - 1;
            }
        }
        return existStationIndex;
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
            if (station.getId() == id && !station.equals(stations.get(stations.size() - 1))) {
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

    public List<String> getStationNames() {
        return stations.stream()
                .map(station -> station.getName())
                .collect(Collectors.toList());
    }

    public boolean remove(Station station) {
        return stations.remove(station);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getStationsSize() {
        return stations.size();
    }
}
