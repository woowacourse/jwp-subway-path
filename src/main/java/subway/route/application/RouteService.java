package subway.route.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Lines;
import subway.line.repository.LineRepository;
import subway.route.application.dto.RouteDto;
import subway.route.domain.FareCalculator;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteFinderBuilder;
import subway.route.domain.RouteSegment;
import subway.route.domain.jgraph.DistanceFareCalculator;
import subway.route.domain.jgraph.JgraphRouteFinderBuilder;

import java.util.List;

@Service
@Transactional
public class RouteService {

    private final LineRepository lineRepository;

    @Autowired
    public RouteService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public RouteDto findRoute(Long source, Long destination) {
        final RouteFinderBuilder<RouteSegment> routeFinderBuilder = new JgraphRouteFinderBuilder();
        final Lines allLines = lineRepository.findAllLines();
        final RouteFinder<RouteSegment> routeFinder = routeFinderBuilder.buildRouteFinder(allLines.getLines());

        final List<RouteSegment> route = routeFinder.getRoute(source, destination);
        final FareCalculator fareCalculator = new DistanceFareCalculator();
        final int totalWeight = routeFinder.getTotalWeight(source, destination);
        final int fare = fareCalculator.calculate(totalWeight);

        return DtoMapper.toRouteDto(route, fare);
    }
}
