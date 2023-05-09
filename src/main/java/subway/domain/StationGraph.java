package subway.domain;

import java.util.*;

public class StationGraph {
    private final Line line;
    private final Map<Station, List<StationEdge>> adjacencyStations;

    public StationGraph(final Line line, final Map<Station, List<StationEdge>> adjacencyStations) {
        this.line = line;
        this.adjacencyStations = new HashMap<>(adjacencyStations);
    }

    public StationGraph(final Line line) {
        this(line, new HashMap<>());
    }

    public List<Station> findAllStations() {
        System.out.println(adjacencyStations);
        return new ArrayList<>(adjacencyStations.keySet());
    }

    public double findDistance(Station source, Station destination) {
        return adjacencyStations.get(source).stream()
                .filter(stationEdge -> stationEdge.getDestination().equals(destination))
                .map(StationEdge::getDistance)
                .findAny().orElseThrow(() -> new NoSuchElementException("입력한 역이 존재하지 않습니다."));
    }

    public void addStation(Station source, Station destination, double weight) {
        final StationEdge destinationEdge = new StationEdge(destination, weight);
        final List<StationEdge> sourceAdjacency = adjacencyStations.getOrDefault(source, new ArrayList<>());
        sourceAdjacency.add(destinationEdge);
        adjacencyStations.put(source, sourceAdjacency);

        final StationEdge sourceEdge = new StationEdge(source, weight);
        final List<StationEdge> destinationAdjacency = adjacencyStations.getOrDefault(destination, new ArrayList<>());
        destinationAdjacency.add(sourceEdge);
        adjacencyStations.put(destination, destinationAdjacency);
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
