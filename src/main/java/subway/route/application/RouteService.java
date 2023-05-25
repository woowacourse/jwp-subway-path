package subway.route.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Lines;
import subway.line.repository.LineRepository;
import subway.route.application.dto.RouteDto;
import subway.route.application.dto.RouteReadDto;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteFinderBuilder;
import subway.route.domain.RouteSegment;
import subway.route.domain.fare.FareFactors;
import subway.route.domain.fare.FarePolicy;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private final FarePolicy farePolicy;
    private final RouteFinderBuilder<RouteSegment> routeFinderBuilder;

    @Autowired
    public RouteService(StationService stationService, LineRepository lineRepository, FarePolicy farePolicy, RouteFinderBuilder<RouteSegment> routeFinderBuilder) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.farePolicy = farePolicy;
        this.routeFinderBuilder = routeFinderBuilder;
    }

    public RouteDto findRoute(RouteReadDto routeReadDto) {
        final Station source = stationService.findStationById(routeReadDto.getSource());
        final Station destination = stationService.findStationById(routeReadDto.getDestination());

        final RouteFinder<RouteSegment> routeFinder = getRouteFinder();
        final List<RouteSegment> route = routeFinder.getRoute(source, destination);

        final int fare = calculateFare(source, destination, routeFinder, routeReadDto);

        return DtoMapper.toRouteDto(route, fare);
    }

    private RouteFinder<RouteSegment> getRouteFinder() {
        final Lines allLines = lineRepository.findAllLines();
        return routeFinderBuilder.buildRouteFinder(allLines.getLines());
    }

    private int calculateFare(Station source, Station destination, RouteFinder<RouteSegment> routeFinder, RouteReadDto routeReadDto) {
        final List<RouteSegment> route = routeFinder.getRoute(source, destination);
        final int distance = routeFinder.getTotalWeight(source, destination);
        final FareFactors fareFactors = new FareFactors();

        farePolicy.buildFareFactors(fareFactors, route, distance, routeReadDto.getAge());
        return farePolicy.calculate(fareFactors, 0);
    }
}
