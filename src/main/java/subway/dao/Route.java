package subway.dao;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.GlobalException;

public class Route {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> route;

    public Route(List<Section> sections) {
        validate(sections);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sections, graph);
        addEdgeWeight(sections, graph);

        this.route = new DijkstraShortestPath<>(graph);
    }

    private void validate(final List<Section> sections) {
        if (sections.isEmpty()) {
            throw new GlobalException("구간은 최소 한개가 필요합니다.");
        }
    }

    private static void addEdgeWeight(final List<Section> sections,
                                      final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(
                            section.getStartStation(),
                            section.getEndStation()),
                    section.getDistance().getDistance()
            );
        }
    }

    private static void addVertex(final List<Section> sections,
                                  final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (int i = 0; i < sections.size(); i++) {
            graph.addVertex(sections.get(i).getStartStation());
            if (i == sections.size() - 1) {
                graph.addVertex(sections.get(i).getEndStation());
            }
        }
    }

    public List<Station> getPath(final Station startStation, final Station endStation) {
        return route.getPath(startStation, endStation).getVertexList();
    }

    public int getPathWeight(final Station startStation, final Station endStation) {
        return (int) route.getPathWeight(startStation, endStation);
    }

}
