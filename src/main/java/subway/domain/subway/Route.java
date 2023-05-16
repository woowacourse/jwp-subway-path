package subway.domain.subway;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.common.Fee;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Route {

    private final Lines lines;
    private final Fee fee;

    private Route(final Lines lines, final Fee fee) {
        this.lines = lines;
        this.fee = fee;
    }

    public static Route from(final Lines lines) {
        return new Route(lines, Fee.from());
    }

    public Map<Station, Set<String>> findShortestPath(final String start, final String destination) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = initGraph();

        GraphPath<String, DefaultWeightedEdge> path = calculateShortestPath(graph, start, destination);
        fee.calculateFromDistance((int) path.getWeight());

        return getStationWithLineNames(path);
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> initGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines.getLines()) {
            fillGraph(graph, line);
        }

        return graph;
    }

    private static void fillGraph(final WeightedMultigraph<String, DefaultWeightedEdge> graph, final Line line) {
        for (Section section : line.getSections()) {
            String upStation = section.getUpStation().getName();
            String downStation = section.getDownStation().getName();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private Map<Station, Set<String>> getStationWithLineNames(final GraphPath<String, DefaultWeightedEdge> path) {
        Map<String, Station> stationsFromName = lines.getStationsFromNameMap();
        Map<Station, Set<String>> lineNamesFromStation = new LinkedHashMap<>();

        for (String stationName : path.getVertexList()) {
            Station station = stationsFromName.get(stationName);
            lineNamesFromStation.put(station, lines.getLinesNameFromStation(station));
        }
        return lineNamesFromStation;
    }

    private GraphPath<String, DefaultWeightedEdge> calculateShortestPath(final WeightedMultigraph<String, DefaultWeightedEdge> graph,
                                                                         final String start,
                                                                         final String destination
    ) {
        DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(start, destination);
    }

    public int getFee() {
        return fee.getFee();
    }
}
