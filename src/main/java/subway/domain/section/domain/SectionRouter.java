package subway.domain.section.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.entity.SectionDetailEntity;
import subway.domain.station.entity.StationEntity;

import java.util.List;

public class SectionRouter {

    private final WeightedMultigraph<StationEntity, DefaultWeightedEdge> graph;

    private SectionRouter(final WeightedMultigraph<StationEntity, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SectionRouter of(final List<SectionDetailEntity> sectionDetails) {
        WeightedMultigraph<StationEntity, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (SectionDetailEntity sectionDetailEntity : sectionDetails) {
            if (!graph.containsVertex(sectionDetailEntity.getUpStation())) {
                graph.addVertex(sectionDetailEntity.getUpStation());
            }
            if (!graph.containsVertex(sectionDetailEntity.getDownStation())) {
                graph.addVertex(sectionDetailEntity.getDownStation());
            }

            graph.setEdgeWeight(graph.addEdge(sectionDetailEntity.getUpStation(), sectionDetailEntity.getDownStation()), sectionDetailEntity.getDistance());
        }

        return new SectionRouter(graph);
    }

    public List<StationEntity> findShortestPath(StationEntity startStation, StationEntity endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);

        GraphPath path = shortestPath.getPath(startStation, endStation);

        return path.getVertexList();
    }

    public double findShortestDistance(StationEntity startStation, StationEntity endStation) {
        DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(graph);

        GraphPath path = shortestPath.getPath(startStation, endStation);

        return path.getWeight();
    }
}
