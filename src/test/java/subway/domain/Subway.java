package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Subway {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath shortestPath;
    private final Map<String, Station> stations;

    public Subway(final List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        insertStation(sections);
        shortestPath = new DijkstraShortestPath(graph);
        stations = stationByName(sections);
    }

    private void insertStation(final List<Section> sections) {
        insertVertex(sections);
        insertEdge(sections);
    }

    private void insertVertex(final List<Section> sections) {
        getStations(sections)
                .forEach(station -> graph.addVertex(station.getName()));
    }

    private List<Station> getStations(final List<Section> sections) {
        final Set<Station> stations = new HashSet<>();
        sections.forEach(section -> {
            stations.add(section.getLeft());
            stations.add(section.getRight());
        });
        return new ArrayList<>(stations);
    }

    private void insertEdge(final List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(
                graph.addEdge(section.getLeft().getName(), section.getRight().getName()),
                section.getDistance().getDistance()
        ));
    }

    private Map<String, Station> stationByName(final List<Section> sections) {
        return getStations(sections).stream()
                .collect(Collectors.toMap(Station::getName, station -> station));
    }

    public long shortestDistanceBetween(final Station from, final Station to) {
        return Math.round(shortestPath.getPathWeight(from.getName(), to.getName()));
    }

    public List<Station> shortestPathBetween(final Station from, final Station to) {
        final List<String> pathNames = shortestPath.getPath(from.getName(), to.getName()).getVertexList();
        return pathNames.stream()
                .map(pathName -> stations.get(pathName))
                .collect(Collectors.toList());
    }
}
