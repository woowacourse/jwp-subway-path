package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.path.CostCalculator;
import subway.domain.path.ShortestPath;
import subway.domain.path.ShortestStationGraph;
import subway.domain.path.StationGraph;
import subway.domain.station.Station;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
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
        StationGraph stationGraph = new ShortestStationGraph(lines, stations);
        ShortestPath shortestPath = stationGraph.getShortestPath(startStation, endStation);

        List<StationResponse> stationResponses = shortestPath
                .getPath()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, shortestPath.getDistance(), CostCalculator.calculateCost(shortestPath.getDistance()));
    }
}
