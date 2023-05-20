package subway.application;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.path.CostCalculator;
import subway.domain.path.LineRecordedDefaultWeightedEdge;
import subway.domain.path.Path;
import subway.domain.station.Station;
import subway.dto.PathResponse;
import subway.dto.SectionResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPathWithCost(Long startStationId, Long endStationId) {
        Station startStation = stationRepository.findById(startStationId);
        Station endStation = stationRepository.findById(endStationId);
        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        Path path = new Path(lines, stations);

        GraphPath<Station, LineRecordedDefaultWeightedEdge> dijkstraShortestPath = path.getDijkstraShortestPath(startStation, endStation);
        List<SectionResponse> sectionResponses = dijkstraShortestPath.getEdgeList()
                .stream()
                .map(lineRecordedDefaultWeightedEdge -> new SectionResponse(
                        lineRecordedDefaultWeightedEdge.getLineId(),
                        path.getEdgeSource(lineRecordedDefaultWeightedEdge).getId(),
                        path.getEdgeTarget(lineRecordedDefaultWeightedEdge).getId(),
                        path.getEdgeWeight(lineRecordedDefaultWeightedEdge)))
                .collect(Collectors.toList());
        return new PathResponse(sectionResponses, (int) dijkstraShortestPath.getWeight(), CostCalculator.calculateCost((int) dijkstraShortestPath.getWeight()));
    }
}
