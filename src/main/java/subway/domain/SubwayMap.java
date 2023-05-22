package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayMap {

    private final WeightedMultigraph<Long, SectionForEdge> graph;

    public SubwayMap() {
        this.graph = new WeightedMultigraph<>(SectionForEdge.class);
    }

    public SubwayMap(List<Section> sections) {
        this.graph = new WeightedMultigraph<>(SectionForEdge.class);
        init(sections);
    }

    private void init(final List<Section> sections) {
        for (Section section : sections) {
            addSection(section);
        }
    }

    public void addSection(Section section) {
        graph.addVertex(section.getUpStationId());
        graph.addVertex(section.getDownStationId());
        SectionForEdge sectionForEdge = SectionForEdge.of(section);
        graph.addEdge(section.getUpStationId(), section.getDownStationId(), sectionForEdge);
        graph.setEdgeWeight(sectionForEdge, section.getDistance());
    }

    public boolean containsSection(Long upStationId, Long downStationId) {
        return graph.containsEdge(upStationId, downStationId);
    }

    public List<Long> findShortestPathIds(Long startStationId, Long endStationId) {
        DijkstraShortestPath<Long, SectionForEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(startStationId, endStationId)
                .getVertexList();
    }

    public Double getTotalDistance(Long startStationId, Long endStationId) {
        DijkstraShortestPath<Long, SectionForEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPathWeight(startStationId, endStationId);
    }
}
