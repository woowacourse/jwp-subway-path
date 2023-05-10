package subway.domain;

import java.util.*;

public class StationGraph {
    private final Line line;
    private final Map<Station, List<StationEdge>> adjacencyStations;

    public StationGraph(final Line line, final Map<Station, List<StationEdge>> adjacencyStations) {
        this.line = line;
        this.adjacencyStations = new LinkedHashMap<>(adjacencyStations);
    }

    public StationGraph(final Line line) {
        this(line, new LinkedHashMap<>());
    }

    public List<Station> findAllStations() {
        return new ArrayList<>(adjacencyStations.keySet());
    }

    public double findDistance(Station source, Station destination) {
        return adjacencyStations.get(source).stream()
                .filter(stationEdge -> stationEdge.getDestination().equals(destination))
                .map(StationEdge::getDistance)
                .findAny().orElseThrow(() -> new NoSuchElementException("입력한 역이 존재하지 않습니다."));
    }

    public void createNewLine(Station upLineStation, Station downLineStation, int distance) {
        final StationEdge downLineEdge = new StationEdge(downLineStation, distance);
        final List<StationEdge> upLineAdjacent = adjacencyStations.getOrDefault(upLineStation, new ArrayList<>());
        upLineAdjacent.add(downLineEdge);
        adjacencyStations.put(upLineStation, upLineAdjacent);

        final StationEdge upLineEdge = new StationEdge(upLineStation, distance);
        final List<StationEdge> downLineAdjacent = adjacencyStations.getOrDefault(downLineStation, new ArrayList<>());
        downLineAdjacent.add(upLineEdge);
        adjacencyStations.put(downLineStation, downLineAdjacent);
    }

    public void addStation(Station newStation, Station existingStation, int distance) {
        // 종점에 새로 연결할 때
        final StationEdge existingEdge = new StationEdge(existingStation, distance);
        final List<StationEdge> newStationAdjacent = adjacencyStations.getOrDefault(newStation, new ArrayList<>());
        newStationAdjacent.add(existingEdge);
        adjacencyStations.put(newStation, newStationAdjacent);

//        final StationEdge newStationEdge = new StationEdge(newStation, distance);
//        final List<StationEdge> existingStationAdjacent = adjacencyStations.getOrDefault(existingStation, new ArrayList<>());
//        existingStationAdjacent.add(newStationEdge);
//        adjacencyStations.put(existingStation, existingStationAdjacent);
    }

    public void addStation(Station newStation, Station adjacentStation1, Station adjacentStation2, int distance) {
        // 두 역 사이에 연결할 때

    }

    public void removeStation(Station station) {
        adjacencyStations.values().forEach(s -> s.remove(station));
    }

    public boolean isSameLine(Line line) {
        return this.line.equals(line);
    }

    @Override
    public String toString() {
        return "StationGraph{" +
                "line=" + line +
                ", adjacencyStations=" + adjacencyStations +
                '}';
    }
}
