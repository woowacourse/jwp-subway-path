package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.SubwayGraph;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public List<String> findShortestPath(Long source, Long target) {
        Lines lines = lineService.findAll();
        SubwayGraph subwayGraph = new SubwayGraph();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        List<Station> shortestPaths = subwayGraph.findPath(sourceStation, targetStation, lines);
        return mapToString(shortestPaths);
    }

    private List<String> mapToString(List<Station> shortestPaths) {
        return shortestPaths.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }
}
