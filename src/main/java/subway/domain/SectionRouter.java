package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.entity.SectionEntity;

import java.util.List;

public class SectionRouter {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    private SectionRouter(final WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SectionRouter of(final List<SectionEntity> sectionDetails) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (SectionEntity sectionDetailEntity : sectionDetails) {
            if (!graph.containsVertex(sectionDetailEntity.getUpStationId())) {
                graph.addVertex(sectionDetailEntity.getUpStationId());
            }
            if (!graph.containsVertex(sectionDetailEntity.getDownStationId())) {
                graph.addVertex(sectionDetailEntity.getDownStationId());
            }

            graph.setEdgeWeight(graph.addEdge(sectionDetailEntity.getUpStationId(), sectionDetailEntity.getDownStationId()), sectionDetailEntity.getDistance());
        }

        return new SectionRouter(graph);
    }

    public List<Long> findShortestPath(Long startStation, Long endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);

        GraphPath path = shortestPath.getPath(startStation, endStation);

        return path.getVertexList();
    }

    public double findShortestDistance(Long startStation, Long endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);

        GraphPath path = shortestPath.getPath(startStation, endStation);

        return path.getWeight();
    }
}
