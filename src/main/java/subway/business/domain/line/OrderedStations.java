package subway.business.domain.line;

import java.util.LinkedList;
import java.util.List;

public class OrderedStations {
    private final List<Station> stations = new LinkedList<>();

    public static OrderedStations from(List<Section> orderedSections) {
        OrderedStations orderedStations = new OrderedStations();
        for (Section section : orderedSections) {
            orderedStations.add(section.getUpwardStation());
        }
        orderedStations.add(orderedSections.get(orderedSections.size() - 1).getDownwardStation());
        return orderedStations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public boolean hasStationNameOf(Station targetStation) {
        for (Station station : stations) {
            if (station.hasNameOf(targetStation.getName())) {
                return true;
            }
        }
        return false;
    }

    public Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.hasNameOf(stationName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                                String.format("존재하지 않는 이름의 역입니다. " + "(입력한 역 이름 : %s)", stationName)
                        )
                );
    }
}
