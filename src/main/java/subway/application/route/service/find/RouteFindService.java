package subway.application.route.service.find;

import org.springframework.stereotype.Service;
import subway.application.route.RouteDtoAssembler;
import subway.application.route.port.in.find.RouteAllEdgesUseCase;
import subway.application.route.port.in.find.RouteFindRequestDto;
import subway.application.route.port.in.find.RouteFindResponseDto;
import subway.application.route.port.in.find.RouteFindUseCase;
import subway.application.route.port.out.find.PathCalculator;
import subway.application.route.port.out.find.PathRequestDto;
import subway.application.route.port.out.find.PathResponseDto;
import subway.domain.route.Edges;
import subway.domain.route.FareCalculator;

@Service
public class RouteFindService implements RouteFindUseCase {

    private final RouteAllEdgesUseCase routeAllEdgesUseCase;
    private final PathCalculator pathCalculator;

    public RouteFindService(final RouteAllEdgesUseCase routeAllEdgesUseCase, final PathCalculator pathCalculator) {
        this.routeAllEdgesUseCase = routeAllEdgesUseCase;
        this.pathCalculator = pathCalculator;
    }

    @Override
    public RouteFindResponseDto findRoute(final RouteFindRequestDto requestDto) {
        final Edges allEdges = routeAllEdgesUseCase.findAllEdges();
        final PathResponseDto pathResponseDto = pathCalculator.calculatePath(
                new PathRequestDto(requestDto.getSource(), requestDto.getTarget(), allEdges));
        final FareCalculator fareCalculator = new FareCalculator();
        final long fare = fareCalculator.calculateFare(pathResponseDto.getDistance());
        return new RouteFindResponseDto(RouteDtoAssembler.toRouteEdgeResponseDto(pathResponseDto.getStations()),
                pathResponseDto.getDistance(), fare);
    }
}
