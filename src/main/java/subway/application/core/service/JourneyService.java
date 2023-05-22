package subway.application.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import subway.application.core.domain.Fare;
import subway.application.core.domain.Line;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.core.service.dto.out.PathFindResult;
import subway.application.core.service.dto.out.StationResult;
import subway.application.port.LineRepository;
import subway.application.port.PathFinder;
import subway.application.port.StationRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional(readOnly = true)
public class JourneyService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public JourneyService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public JourneyResult findShortestJourney(@Valid JourneyCommand journeyCommand) {
        List<RouteMap> routeMaps = getAllRouteMaps();
        PathFindResult result = findShortestPath(journeyCommand, routeMaps);
        return new JourneyResult(mapToStationResults(result), result.getDistance(),
                new Fare(result.getDistance()).value());
    }

    private List<StationResult> mapToStationResults(PathFindResult result) {
        return result.getShortestPath().stream()
                .map(StationResult::new)
                .collect(Collectors.toList());
    }

    private PathFindResult findShortestPath(JourneyCommand journeyCommand, List<RouteMap> routeMaps) {
        Station departure = stationRepository.findById(journeyCommand.getDeparture());
        Station terminal = stationRepository.findById(journeyCommand.getTerminal());
        return pathFinder.findShortestPath(routeMaps, departure, terminal);
    }

    private List<RouteMap> getAllRouteMaps() {
        return lineRepository.findAll().stream()
                .map(Line::routeMap)
                .collect(Collectors.toList());
    }
}
