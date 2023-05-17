package subway.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

public class PathFinder {
    private final Graph<Long, DefaultWeightedEdge> graph;

    public PathFinder(List<Section> allSections) {
        Set<Section> sections = new HashSet<>(allSections);
        this.graph = makeGraph(sections);
    }

    private Graph<Long, DefaultWeightedEdge> makeGraph(Set<Section> sections) {
        Graph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setGraph(sections, graph);
        return graph;
    }

    private void setGraph(Set<Section> allSections, Graph<Long, DefaultWeightedEdge> graph) {
        for (Section section : allSections) {
            Long sourceStationId = section.getSourceStationId();
            Long targetStationId = section.getTargetStationId();
            graph.addVertex(sourceStationId);
            graph.addVertex(targetStationId);
            graph.setEdgeWeight(graph.addEdge(sourceStationId, targetStationId), section.getDistance());
            graph.setEdgeWeight(graph.addEdge(targetStationId, sourceStationId), section.getDistance());
        }
    }

    public List<Long> find(Long departureId, Long destinationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(departureId, destinationId);

        if (path == null) {
            throw new DomainException(ExceptionType.NO_PATH);
        }

        return path
            .getVertexList()
            .stream()
            .collect(Collectors.toUnmodifiableList());
    }
}
