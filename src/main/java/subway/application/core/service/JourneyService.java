package subway.application.core.service;

import org.springframework.stereotype.Service;
import subway.application.core.domain.Fare;
import subway.application.core.domain.Line;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.in.JourneyCommand;
import subway.application.core.service.dto.out.JourneyResult;
import subway.application.core.service.dto.out.PathFindResult;
import subway.application.port.LineRepository;
import subway.application.port.PathFinder;
import subway.application.port.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JourneyService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public JourneyService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public JourneyResult findShortestJourney(JourneyCommand journeyCommand) {
        List<RouteMap> routeMaps = getAllRouteMaps();
        Station departure = stationRepository.findById(journeyCommand.getDeparture());
        Station terminal = stationRepository.findById(journeyCommand.getTerminal());
        PathFindResult result = pathFinder.findShortestPath(routeMaps, departure, terminal);

        return new JourneyResult(
                result.getShortestPath(),
                result.getDistance(),
                new Fare(result.getDistance()).value()
        );
    }

    private List<RouteMap> getAllRouteMaps() {
        return lineRepository.findAll().stream()
                .map(Line::routeMap)
                .collect(Collectors.toList());
    }
}
