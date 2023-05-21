package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.StationNotFoundException;

public class PathFinder {

    private final Subway subway;

    public PathFinder(final Subway subway) {
        this.subway = subway;
    }

    public Path findShortestPath(final String startStationName, final String endStationName) {
        if (!isExistStation(startStationName) || !isExistStation(endStationName)) {
            throw new StationNotFoundException();
        }

        final WeightedMultigraph<String, DefaultWeightedEdge> graph = makeWeightGraph();
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph);
        final List<String> pathWithStationName = dijkstraShortestPath.getPath(startStationName, endStationName)
                .getVertexList();
        final double distance = dijkstraShortestPath.getPathWeight(startStationName, endStationName);
        final List<Station> stations = pathWithStationName.stream()
                .map(Station::new)
                .collect(Collectors.toList());

        return new Path(stations, new Distance((int) distance));
    }

    private Boolean isExistStation(final String stationName){
        return subway.getLines().stream()
                .anyMatch(line -> line.hasStation(stationName));
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> makeWeightGraph() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : subway.getLines()) {
            makeGraphEachLine(graph, line);
        }
        return graph;
    }

    private void makeGraphEachLine(final WeightedMultigraph<String, DefaultWeightedEdge> graph, final Line line) {
        final List<Section> sections = line.getSections();
        for (Section section : sections) {
            final String startName = section.getStartName();
            final String endName = section.getEndName();
            final int distance = section.getDistanceValue();

            addVertexIfNotContains(graph, startName);
            addVertexIfNotContains(graph, endName);
            graph.setEdgeWeight(graph.addEdge(startName, endName), distance);
        }
    }

    private void addVertexIfNotContains(
            final WeightedMultigraph<String, DefaultWeightedEdge> graph,
            final String stationName
    ) {
        if (!graph.containsVertex(stationName)) {
            graph.addVertex(stationName);
        }
    }
}
