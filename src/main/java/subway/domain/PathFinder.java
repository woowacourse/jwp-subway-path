package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Distance;
import subway.domain.station.Station;

import java.util.List;

public class PathFinder {

    private final SubwayMap subwayMap;

    public PathFinder(final SubwayMap subwayMap) {
        this.subwayMap = subwayMap;
    }

    public Path findShortest(final Station from, final Station to) {
        final WeightedMultigraph<Station, SectionEdge> graph = generateGraph();
        final GraphPath<Station, SectionEdge> path = findPath(from, to, graph);
        return new Path(path.getEdgeList(), new Distance((int) path.getWeight()));
    }

    private WeightedMultigraph<Station, SectionEdge> generateGraph() {
        final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addStations(graph, subwayMap.getStations());
        addSectionEdge(graph, subwayMap.getSectionEdge());
        return graph;
    }

    private void addStations(final WeightedMultigraph<Station, SectionEdge> graph, final List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addSectionEdge(final WeightedMultigraph<Station, SectionEdge> graph, final List<SectionEdge> sectionEdges) {
        sectionEdges.forEach(section -> {
            graph.addEdge(section.getFrom(), section.getTo(), section);
            graph.setEdgeWeight(section, section.getDistanceValue());
        });
    }

    private GraphPath<Station, SectionEdge> findPath(final Station from, final Station to, final WeightedMultigraph<Station, SectionEdge> graph) {
        final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(from, to);
    }
}
