package subway.domain.subway;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.common.Fee;
import subway.exception.LinesEmptyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Route {

    private WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private Lines lines;
    private final Fee fee;

    private Route(final Lines lines, final Fee fee) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
        this.fee = fee;
    }

    public static Route from(final Lines lines) {
        return new Route(lines, Fee.createDefault());
    }

    public static Route createDefault() {
        return new Route(null, Fee.createDefault());
    }

    public List<Station> findShortestPath(final String start, final String destination) {
        validateEmptyLines();
        initGraph();
        GraphPath<String, DefaultWeightedEdge> shortestPath = calculateShortestPath(start, destination);
        return findStationsFromPath(shortestPath);
    }

    private void validateEmptyLines() {
        if (lines == null || lines.isEmptyLines()) {
            throw new LinesEmptyException();
        }
    }

    private void initGraph() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines.getLines()) {
            fillGraph(line);
        }
    }

    private void fillGraph(final Line line) {
        for (Section section : line.getSections()) {
            String upStation = section.getUpStation().getName();
            String downStation = section.getDownStation().getName();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private GraphPath<String, DefaultWeightedEdge> calculateShortestPath(final String start, final String destination) {
        DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = shortestPath.getPath(start, destination);
        fee.calculateFromDistance((int) path.getWeight());
        return path;
    }

    private List<Station> findStationsFromPath(final GraphPath<String, DefaultWeightedEdge> path) {
        Map<String, Station> stationsByName = lines.getStationsByNameInfo();
        List<Station> stations = new ArrayList<>();

        for (String stationName : path.getVertexList()) {
            Station station = stationsByName.get(stationName);
            stations.add(station);
        }

        return stations;
    }

    public List<Set<String>> findShortestTransferLines(final String start, final String destination) {
        validateEmptyLines();
        initGraph();
        GraphPath<String, DefaultWeightedEdge> shortestPath = calculateShortestPath(start, destination);
        return findTransferLines(shortestPath);
    }

    private List<Set<String>> findTransferLines(final GraphPath<String, DefaultWeightedEdge> path) {
        Map<String, Station> stationsByName = lines.getStationsByNameInfo();
        List<Set<String>> transferLines = new ArrayList<>();

        for (String stationName : path.getVertexList()) {
            Station station = stationsByName.get(stationName);
            transferLines.add(lines.getLineNamesFromStation(station));
        }

        return transferLines;
    }

    public void update(final Lines lines) {
        this.lines = lines;
    }

    public int getFee() {
        return fee.getFee();
    }
}
