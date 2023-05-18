package subway.application.core.service;

import org.springframework.stereotype.Service;
import subway.application.core.domain.Line;
import subway.application.core.domain.RouteMap;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.core.service.dto.out.StationResult;
import subway.application.port.LineRepository;
import subway.application.port.PathFinder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public SubwayService(LineRepository lineRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public Map<String, List<StationResult>> findAllRouteMap() {
        List<Line> allLines = lineRepository.findAll();
        return allLines.stream()
                .collect(Collectors.toMap(Line::getName, this::makeStationResultsOf));
    }

    private List<StationResult> makeStationResultsOf(Line line) {
        return line.routeMap().stations().stream()
                .map(station -> new StationResult(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public JourneyResult findShortestJourney() {
        List<RouteMap> routeMaps = lineRepository.findAll().stream()
                .map(Line::routeMap)
                .collect(Collectors.toList());

        return new JourneyResult(
                pathFinder.findShortestPath(routeMaps),
                pathFinder.calculateDistance(routeMaps)
        );
    }
}
