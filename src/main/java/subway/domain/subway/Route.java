package subway.domain.subway;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.common.Fee;
import subway.exception.LinesEmptyException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Route {

    private Lines lines;
    private final Fee fee;

    private Route(final Lines lines, final Fee fee) {
        this.lines = lines;
        this.fee = fee;
    }

    public static Route from(final Lines lines) {
        return new Route(lines, Fee.createDefault());
    }

    public static Route createDefault() {
        return new Route(null, Fee.createDefault());
    }

    public Map<Station, Set<String>> findShortestPath(final String start, final String destination) {
        validateEmptyLines();

        WeightedMultigraph<String, DefaultWeightedEdge> graph = initGraph();

        GraphPath<String, DefaultWeightedEdge> shortestPath = calculateShortestPath(graph, start, destination);
        fee.calculateFromDistance((int) shortestPath.getWeight());

        return findLineNamesByStation(shortestPath);
    }

    private void validateEmptyLines() {
        if (this.lines == null) {
            throw new LinesEmptyException();
        }
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> initGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines.getLines()) {
            fillGraph(graph, line);
        }

        return graph;
    }

    private void fillGraph(final WeightedMultigraph<String, DefaultWeightedEdge> graph, final Line line) {
        for (Section section : line.getSections()) {
            String upStation = section.getUpStation().getName();
            String downStation = section.getDownStation().getName();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private Map<Station, Set<String>> findLineNamesByStation(final GraphPath<String, DefaultWeightedEdge> path) {
        Map<String, Station> StationsByName = lines.getStationsByName();
        Map<Station, Set<String>> lineNamesByStation = new LinkedHashMap<>();

        for (String stationName : path.getVertexList()) {
            Station station = StationsByName.get(stationName);
            lineNamesByStation.put(station, lines.getLineNamesByStation(station));
        }

        return lineNamesByStation;
    }

    private GraphPath<String, DefaultWeightedEdge> calculateShortestPath(final WeightedMultigraph<String, DefaultWeightedEdge> graph,
                                                                         final String start,
                                                                         final String destination
    ) {
        DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(start, destination);
    }

    public void update(final Lines lines) {
        this.lines = lines;
    }

    public int getFee() {
        return fee.getFee();
    }
}
