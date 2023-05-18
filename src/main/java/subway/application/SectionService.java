package subway.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.domain.Section;
import subway.domain.Station;
import subway.persistence.repository.SectionRepository;
import subway.ui.request.PathRequest;
import subway.ui.response.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SectionService {

    private final SectionRepository serviceRepository;

    public SectionService(final SectionRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<StationResponse> findPath(final PathRequest pathRequest) {
        final List<Section> sections = serviceRepository.findAll();
        try {
            final List<Station> shortestPath = findShortestPath(sections, pathRequest);
            return shortestPath.stream()
                    .map(StationResponse::new)
                    .collect(Collectors.toList());
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
    }

    private List<Station> findShortestPath(final List<Section> sections, final PathRequest pathRequest) {
        final Map<String, Station> stationMap = getStationMap(sections);
        final Station startStation = stationMap.get(pathRequest.getStartStationName());
        final Station endStation = stationMap.get(pathRequest.getEndStationName());

        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stationMap.values().forEach(graph::addVertex);
        sections.forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getBeforeStation(), section.getNextStation()),
                        section.getDistance().getValue()
                )
        );

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
    }

    private Map<String, Station> getStationMap(final List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getBeforeStation(), section.getNextStation()))
                .distinct().collect(Collectors.toMap(Station::getName, Function.identity()));
    }
}
