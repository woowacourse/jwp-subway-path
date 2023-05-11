package subway.domain;

import java.util.*;

public class StationConverter {
    public static List<Station> getStations(List<PairStation> pairStations) {
        if (pairStations.isEmpty()) {
            throw new RuntimeException("구간이 존재하지 않습니다");
        }

        return getSortedStations(pairStations);
    }

    private static List<Station> getSortedStations(List<PairStation> pairStations) {
        Deque<Station> result = new ArrayDeque<>();
        Map<Station, Station> upStationToFindDown = new HashMap<>();
        Map<Station, Station> downStationToFindUp = new HashMap<>();
        setMapToFindStations(pairStations, upStationToFindDown, downStationToFindUp);

        Station pivotStation = pairStations.get(0).getPreStation();
        result.add(pivotStation);
        sortStations(result, upStationToFindDown, downStationToFindUp);
        return new ArrayList<>(result);
    }

    private static void sortStations(Deque<Station> result, Map<Station, Station> upStationToFindDown, Map<Station, Station> downStationToFindUp) {
        while (downStationToFindUp.containsKey(result.peekFirst())) {
            Station current = result.peekFirst();
            result.addFirst(downStationToFindUp.get(current));
        }
        while (upStationToFindDown.containsKey(result.peekLast())) {
            Station current = result.peekLast();
            result.addLast(upStationToFindDown.get(current));
        }
    }

    private static void setMapToFindStations(List<PairStation> pairStations, Map<Station, Station> upStationToFindDown, Map<Station, Station> downStationToFindUp) {
        for (PairStation station : pairStations) {
            upStationToFindDown.put(station.getPreStation(), station.getStation());
            downStationToFindUp.put(station.getStation(), station.getPreStation());
        }
    }
}
