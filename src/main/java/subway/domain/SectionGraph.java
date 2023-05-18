package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SectionGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final Map<String, Station> stationMap;

    public SectionGraph(final List<Section> sections) {
        this.stationMap = extractStationAsMap(sections);
        this.graph = createGraph(sections);
    }

    private static Map<String, Station> extractStationAsMap(final List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getBeforeStation(), section.getNextStation()))
                .distinct()
                .collect(Collectors.toMap(Station::getName, Function.identity()));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(final List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stationMap.values().forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getBeforeStation(), section.getNextStation()),
                        section.getDistance().getValue()
                )
        );

        return graph;
    }

    public List<Station> findShortestPath(final String startStation, final String endStation) {
        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(
                    stationMap.get(startStation),
                    stationMap.get(endStation)
            ).getVertexList();
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
    }
}
