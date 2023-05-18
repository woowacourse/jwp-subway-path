package subway.route.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Lines;
import subway.line.repository.LineRepository;
import subway.route.application.dto.RouteDto;
import subway.route.application.dto.RouteReadDto;
import subway.route.domain.FarePolicy;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteFinderBuilder;
import subway.route.domain.RouteSegment;
import subway.route.domain.jgraph.JgraphRouteFinderBuilder;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    @Autowired
    public RouteService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public RouteDto findRoute(RouteReadDto routeReadDto) {
        final Station source = stationService.findStationById(routeReadDto.getSource());
        final Station destination = stationService.findStationById(routeReadDto.getDestination());

        final RouteFinder<RouteSegment> routeFinder = getRouteFinder();
        final List<RouteSegment> route = routeFinder.getRoute(source, destination);
        final int fare = calculateFare(source, destination, routeFinder);

        return DtoMapper.toRouteDto(route, fare);
    }

    private RouteFinder<RouteSegment> getRouteFinder() {
        final RouteFinderBuilder<RouteSegment> routeFinderBuilder = new JgraphRouteFinderBuilder();
        final Lines allLines = lineRepository.findAllLines();
        return routeFinderBuilder.buildRouteFinder(allLines.getLines());
    }

    private int calculateFare(Station source, Station destination, RouteFinder<RouteSegment> routeFinder) {
        final FareCalculator fareCalculator = new DistanceFareCalculator();
        final int totalWeight = routeFinder.getTotalWeight(source, destination);
        return fareCalculator.calculate(totalWeight);
    }
}
