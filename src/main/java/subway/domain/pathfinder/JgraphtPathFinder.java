package subway.domain.pathfinder;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import subway.domain.Section;

@Component
public class JgraphtPathFinder implements PathFinder {
    WeightedMultigraph<Long, LineWeightedEdge> graph;

    public JgraphtPathFinder(WeightedMultigraph<Long, LineWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void addSections(List<Section> sections) {
        for (Section section : sections) {
            final Long sourceStationId = section.getSourceStationId();
            final Long targetStationId = section.getTargetStationId();
            graph.addVertex(sourceStationId);
            graph.addVertex(targetStationId);
            final LineWeightedEdge edge = graph.addEdge(sourceStationId, targetStationId);
            edge.setLineId(section.getLineId());
            edge.setDistance(section.getDistance());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    @Override
    public List<Section> computeShortestPath(Long sourceStationId, Long targetStationId) {
        final var dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId)
                .getEdgeList()
                .stream()
                .map(edge -> new Section(graph.getEdgeSource(edge), graph.getEdgeTarget(edge), edge.getLineId(),
                        edge.getDistance()))
                .collect(Collectors.toUnmodifiableList());

    }

    @Override
    public Integer computeShortestDistance(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, LineWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPathWeight(sourceStationId, targetStationId);
    }
}
