package subway.domain.station;

import subway.domain.section.Section;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

public class StationConnections {

    private final Map<Station, Station> stationConnections;

    private StationConnections(Map<Station, Station> stationConnections) {
        this.stationConnections = stationConnections;
    }

    public static StationConnections fromSections(List<Section> findSections) {
        Map<Station, Station> stationConnections = findSections.stream()
                .collect(toMap(Section::getUpStation, Section::getDownStation));
        return new StationConnections(stationConnections);
    }

    public List<String> getSortedStationNames() {
        List<String> sortedStationNames = new ArrayList<>();
        Station upEndStation = findUpEndStation();
        sortedStationNames.add(upEndStation.getName());

        Station tempUpStation = upEndStation;
        for (int i = 0; i < stationConnections.size(); i++) {
            Station downStation = stationConnections.get(tempUpStation);
            sortedStationNames.add(downStation.getName());
            tempUpStation = downStation;
        }
        return sortedStationNames;
    }

    private Station findUpEndStation() {
        List<Station> upStations = new ArrayList<>(stationConnections.keySet());
        List<Station> downStations = new ArrayList<>(stationConnections.values());
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new StationNotFoundException("상행 종점을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationConnections that = (StationConnections) o;
        return Objects.equals(stationConnections, that.stationConnections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationConnections);
    }

    @Override
    public String toString() {
        return "StationConnections{" +
                "stationConnections=" + stationConnections +
                '}';
    }
}
