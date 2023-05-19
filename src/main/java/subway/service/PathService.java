package subway.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.domain.Price;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final SubwayService subwayService;
    private final StationRepository stationRepository;

    public PathService(final SubwayService subwayService,
                       final StationRepository stationRepository) {
        this.subwayService = subwayService;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        final Subway subway = subwayService.findSubway();

        final Station source = stationRepository.findById(sourceId);
        final Station target = stationRepository.findById(targetId);

        final WeightedMultigraph<Station, DefaultWeightedEdge> allStationGraph = subway.findAllStationGraph();

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(allStationGraph);

        final List<Station> stations = shortestPath.getPath(source, target).getVertexList();

        final List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        double distance = shortestPath.getPathWeight(source, target);

        return new PathResponse(stationResponses, Price.from(distance).getPrice());
    }
}
