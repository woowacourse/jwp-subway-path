package subway.domain.station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StationConnections {

    private final Map<Station, Station> stationConnections;

    public StationConnections(Map<Station, Station> stationConnections) {
        this.stationConnections = stationConnections;
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
        System.out.println("before Remove upStations = " + upStations);
        System.out.println("downStations = " + downStations);
        upStations.removeAll(downStations);
        System.out.println("After Remove upStations = " + upStations);
        System.out.println("upStations Size = " + upStations.size());
        if (upStations.size() != 1) {
            throw new IllegalStateException("상행 종점을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }
}
