package subway.domain.pathfinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import subway.domain.Section;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Component
public class JgraphtPathFinder implements PathFinder {
    WeightedMultigraph<Long, LineWeightedEdge> graph;

    public JgraphtPathFinder(WeightedMultigraph<Long, LineWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void addSections(List<Section> sections) {
        initialize();
        for (Section section : sections) {
            final Long sourceStationId = section.getSourceStationId();
            final Long targetStationId = section.getTargetStationId();
            graph.addVertex(sourceStationId);
            graph.addVertex(targetStationId);
            final LineWeightedEdge lineWeightedEdge = new LineWeightedEdge(section.getLineId(), section.getDistance());
            graph.setEdgeWeight(lineWeightedEdge, section.getDistance());
            graph.addEdge(sourceStationId, targetStationId, lineWeightedEdge);
        }
    }

    private void initialize() {
        final HashSet<Long> stationIds = new HashSet<>(graph.vertexSet());
        for (Long stationId : stationIds) {
            graph.removeVertex(stationId);
        }
    }

    @Override
    public List<Section> computeShortestPath(Long sourceStationId, Long targetStationId) {
        try {
            final var dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            final GraphPath<Long, LineWeightedEdge> path = dijkstraShortestPath.getPath(sourceStationId,
                    targetStationId);
            if (path == null) {
                throw new DomainException(ExceptionType.UN_REACHABLE_PATH);
            }
            return path.getEdgeList()
                    .stream()
                    .map(edge -> new Section(graph.getEdgeSource(edge), graph.getEdgeTarget(edge), edge.getLineId(),
                            edge.getDistance()))
                    .collect(Collectors.toUnmodifiableList());
        } catch (IllegalArgumentException exception) {
            throw new DomainException(ExceptionType.STATION_IS_NOT_IN_SECTION);
        }
    }

    @Override
    public Integer computeShortestDistance(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, LineWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPathWeight(sourceStationId, targetStationId);
    }
}
