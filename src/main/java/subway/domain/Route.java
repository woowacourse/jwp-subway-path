package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
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

    private void addEdgeWeight(final List<Section> sections,
                               final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        try {
            for (Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(
                                section.getStartStation(),
                                section.getEndStation()),
                        section.getDistance().getDistance()
                );
            }
        } catch (IllegalArgumentException exception) {
            throw new GlobalException("연결되어 있지 않은 구간은 조회할 수 없습니다.");
        }
    }

    private void addVertex(final List<Section> sections,
                           final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (int i = 0; i < sections.size(); i++) {
            graph.addVertex(sections.get(i).getStartStation());
            if (i == sections.size() - 1) {
                graph.addVertex(sections.get(i).getEndStation());
            }
        }
    }

    public List<Station> getPath(final Station startStation, final Station endStation) {
        validateStations(startStation, endStation);

        try {
            return route.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException exception) {
            throw new GlobalException("출발역 또는 도착역이 존재하지 않습니다.");
        }
    }

    private void validateStations(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new GlobalException("같은 역으로 경로를 조회할 수 없습니다.");
        }
    }

    public int getPathWeight(final Station startStation, final Station endStation) {
        validateStations(startStation, endStation);
        return (int) route.getPathWeight(startStation, endStation);
    }

}
