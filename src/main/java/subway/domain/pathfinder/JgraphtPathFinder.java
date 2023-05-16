package subway.domain.pathfinder;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import subway.domain.Section;

@Component
public class JgraphtPathFinder implements PathFinder {
    WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public JgraphtPathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void addSections(List<Section> sections) {
        for (Section section : sections) {
            final Long sourceStationId = section.getSourceStationId();
            final Long targetStationId = section.getTargetStationId();
            graph.addVertex(sourceStationId);
            graph.addVertex(targetStationId);
            graph.setEdgeWeight(graph.addEdge(sourceStationId, targetStationId), section.getDistance());
        }
    }

    @Override
    public List<Long> computeShortestPath(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId).getVertexList();
    }

    @Override
    public Integer computeShortestDistance(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) dijkstraShortestPath.getPathWeight(sourceStationId, targetStationId);
    }
}
