package subway.service.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class RouteMapInLine {

    private final Map<Station, List<Path>> map;

    public RouteMapInLine(Map<Station, List<Path>> map) {
        this.map = map;
    }

    public Map<Station, List<Path>> getMap() {
        return map;
    }

    public List<Station> getStationsOnLine() {
        return map.entrySet()
                .stream()
                .findFirst()
                .map(stationEntry -> createSingleLine(stationEntry.getKey()))
                .orElseGet(Collections::emptyList);
    }

    private List<Station> createSingleLine(Station startStation) {
        Set<Station> visitedStation = new HashSet<>();
        Queue<Station> nowStations = new LinkedList<>();
        Deque<Station> singleLine = new LinkedList<>();
        initForCreateSingleLine(startStation, visitedStation, nowStations, singleLine);
        searchLine(visitedStation, nowStations, singleLine);
        return new ArrayList<>(singleLine);
    }

    private void searchLine(Set<Station> visitedStation,
                            Queue<Station> nowStations,
                            Deque<Station> singleLine) {
        while (!nowStations.isEmpty()) {
            Station nowStation = nowStations.poll();
            map.get(nowStation).forEach(path -> selectNextStation(visitedStation, nowStations, singleLine, path));
        }
    }

    private void selectNextStation(Set<Station> visitedStation,
                                   Queue<Station> nowStations,
                                   Deque<Station> singleLine,
                                   Path path) {
        if (visitedStation.contains(path.getNextStation())) {
            return;
        }
        if (Direction.UP == path.getDirection()) {
            singleLine.addLast(path.getNextStation());
        }
        if (Direction.DOWN == path.getDirection()) {
            singleLine.addFirst(path.getNextStation());
        }
        addNextStation(visitedStation, nowStations, path);
    }

    private void addNextStation(Set<Station> visitedStation,
                                Queue<Station> queue,
                                Path path) {
        visitedStation.add(path.getNextStation());
        queue.add(path.getNextStation());
    }

    private void initForCreateSingleLine(Station startStation,
                                         Set<Station> visitedStation,
                                         Queue<Station> queue,
                                         Deque<Station> singleLine) {
        queue.add(startStation);
        visitedStation.add(startStation);
        singleLine.add(startStation);
    }

    @Override
    public String toString() {
        return "RouteMap{" +
                "map=" + map +
                '}';
    }

}
