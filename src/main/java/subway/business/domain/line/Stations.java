package subway.business.domain.line;

import java.util.LinkedList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations createOfOrderedSections(List<Section> orderedSections) {
        Stations stations = new Stations(new LinkedList<>());
        for (Section section : orderedSections) {
            stations.add(section.getUpwardStation());
        }
        stations.add(orderedSections.get(orderedSections.size() - 1).getDownwardStation());
        return stations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public boolean hasStationNameOf(Station targetStation) {
        return stations.stream()
                .anyMatch(station -> station.hasNameOf(targetStation.getName()));
    }

    public Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.hasNameOf(stationName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "존재하지 않는 이름의 역입니다. " + "(입력한 역 이름 : %s)", stationName))
                );
    }

    public List<Station> getStations() {
        return stations;
    }
}
