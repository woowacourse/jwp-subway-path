package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.exception.route.MinimumSectionForRouteException;
import subway.exception.section.DisconnectedSectionException;
import subway.exception.station.DuplicateStationNameException;
import subway.exception.station.NotFoundStationException;

@Component
public class JgraphtForDijkstra implements DijkstraStrategy {

    private void validate(final List<Section> sections) {
        if (sections.isEmpty()) {
            throw new MinimumSectionForRouteException("구간은 최소 한개가 필요합니다.");
        }
    }

    @Override
    public List<Station> getShortestPath(final Sections sections, final Station startStation,
                                         final Station endStation) {
        validateStations(startStation, endStation);
        List<Section> sectionsForPath = sections.getSections();
        DijkstraShortestPath<Station, DefaultWeightedEdge> jGraph = makeJGrapht(
                sectionsForPath);

        try {
            return jGraph.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException exception) {
            throw new NotFoundStationException("출발역 또는 도착역이 존재하지 않습니다.");
        }
    }

    @Override
    public Distance getShortestPathWeight(final Sections sections, final Station startStation,
                                          final Station endStation) {
        validateStations(startStation, endStation);
        List<Section> sectionsForPath = sections.getSections();
        validate(sectionsForPath);
        DijkstraShortestPath<Station, DefaultWeightedEdge> jGraph = makeJGrapht(
                sectionsForPath);

        return new Distance((int) jGraph.getPathWeight(startStation, endStation));
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
            throw new DisconnectedSectionException("연결되어 있지 않은 구간은 조회할 수 없습니다.");
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

    private void validateStations(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new DuplicateStationNameException("같은 역으로 경로를 조회할 수 없습니다.");
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> makeJGrapht(
            final List<Section> sectionsForPath) {
        validate(sectionsForPath);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = makeDijkstra(
                sectionsForPath);

        return new DijkstraShortestPath<>(graph);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeDijkstra(
            final List<Section> sectionsForPath) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sectionsForPath, graph);
        addEdgeWeight(sectionsForPath, graph);
        return graph;
    }
}
