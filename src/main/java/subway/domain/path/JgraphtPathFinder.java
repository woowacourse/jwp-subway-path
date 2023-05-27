package subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.exception.StationNotFoundException;

public class JgraphtPathFinder implements PathFinder {

    private final Subway subway;

    public JgraphtPathFinder(final Subway subway) {
        this.subway = subway;
    }

    @Override
    public Path find(final String startStationName, final String endStationName) {
        validateExist(startStationName, endStationName);

        final WeightedMultigraph<String, DefaultWeightedEdge> graph = makeWeightGraph();
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph);
        final List<String> pathWithStationName = dijkstraShortestPath.getPath(startStationName, endStationName)
                .getVertexList();
        final List<Station> stations = nameToStation(pathWithStationName);
        final double distance = dijkstraShortestPath.getPathWeight(startStationName, endStationName);

        return new Path(stations, new Distance((int) distance));
    }

    private void validateExist(String startStationName, String endStationName) {
        if (!isExistStation(startStationName) || !isExistStation(endStationName)) {
            throw new StationNotFoundException();
        }
    }

    private Boolean isExistStation(final String stationName) {
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

    private List<Station> nameToStation(final List<String> pathWithStationName) {
        return pathWithStationName.stream()
                .map(Station::new)
                .collect(Collectors.toList());
    }
}
