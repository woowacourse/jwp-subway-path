package subway.domain;

import subway.exception.IllegalAddStationException;
import subway.exception.StationAlreadyExistException;
import java.util.LinkedList;
import java.util.List;

public class Stations {

    private static final int MIN_SIZE_TO_ADD_ONE_STATION = 2;

    private final LinkedList<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new LinkedList<>(stations);
    }

    public static Stations emptyStations() {
        return new Stations(new LinkedList<>());
    }

    public void addTwoStation(final Station firstStation, final Station secondStation) {
        stations.addFirst(firstStation);
        stations.addLast(secondStation);
    }

    public void addFirst(final Station newStation) {
        stations.addFirst(newStation);
    }

    public void addBeforeAt(Station targetStation, Station newStation) {
        validateDuplicate(newStation);
        validateStationSize();

        if (stations.contains(targetStation)) {
            int index = stations.indexOf(targetStation);
            stations.add(index, newStation);
        }
    }

    private void validateDuplicate(Station sourceStation) {
        if (stations.contains(sourceStation)) {
            throw new StationAlreadyExistException();
        }
    }

    public void addLast(Station station) {
        validateDuplicate(station);
        validateStationSize();

        stations.addLast(station);
    }

    private void validateStationSize() {
        if (stations.size() < MIN_SIZE_TO_ADD_ONE_STATION) {
            throw new IllegalAddStationException("역이 2개 미만일 때는 하나의 역을 추가할 수 없습니다.");
        }
    }

    public boolean isEmpty() {
        return stations.isEmpty();
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public List<Station> getStations() {
        return List.copyOf(stations);
    }
}
