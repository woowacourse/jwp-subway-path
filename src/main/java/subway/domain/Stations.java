package subway.domain;

import java.util.LinkedList;

public class Stations {

    private final LinkedList<Station> stations;

    public Stations(LinkedList<Station> stations) {
        this.stations = new LinkedList<>(stations);
    }

    public static Stations emptyStations() {
        return new Stations(new LinkedList<>());
    }

    public void addBeforeAt(Station targetStation, Station sourceStation) {
        validateDuplicate(sourceStation);
        if (stations.contains(targetStation)) {
            int index = stations.indexOf(targetStation);
            stations.add(index, sourceStation);
        }
    }

    private void validateDuplicate(Station sourceStation) {
        if (stations.contains(sourceStation)) {
            throw new IllegalStateException("넣으려고 하는 역이 이미 존재합니다.");
        }
    }

    public void addLast(Station station) {
        validateDuplicate(station);
        stations.addLast(station);
    }

    public boolean isEmpty() {
        return stations.size() == 0;
    }

    public boolean contains(Station station) {
        return stations.contains(station);
    }

    public LinkedList<Station> getStations() {
        return new LinkedList<>(stations);
    }
}
