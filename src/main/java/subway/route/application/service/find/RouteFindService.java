package subway.route.application.service.find;

import org.springframework.stereotype.Service;
import subway.route.application.RouteDtoAssembler;
import subway.route.application.port.in.find.RouteAllEdgesUseCase;
import subway.route.application.port.in.find.RouteFindRequestDto;
import subway.route.application.port.in.find.RouteFindResponseDto;
import subway.route.application.port.in.find.RouteFindUseCase;
import subway.route.application.port.out.find.PathCalculator;
import subway.route.application.port.out.find.PathRequestDto;
import subway.route.application.port.out.find.PathResponseDto;
import subway.route.domain.Edges;
import subway.route.domain.FareCalculator;

@Service
public class RouteFindService implements RouteFindUseCase {

    private final RouteAllEdgesUseCase routeAllEdgesUseCase;
    private final PathCalculator pathCalculator;

    public RouteFindService(RouteAllEdgesUseCase routeAllEdgesUseCase, PathCalculator pathCalculator) {
        this.routeAllEdgesUseCase = routeAllEdgesUseCase;
        this.pathCalculator = pathCalculator;
    }

    @Override
    public RouteFindResponseDto findRoute(RouteFindRequestDto requestDto) {
        Edges allEdges = routeAllEdgesUseCase.findAllEdges();
        PathResponseDto pathResponseDto = pathCalculator.calculatePath(
                new PathRequestDto(requestDto.getSource(), requestDto.getTarget(), allEdges));
        FareCalculator fareCalculator = new FareCalculator();
        long fare = fareCalculator.calculateFare(pathResponseDto.getDistance());
        return new RouteFindResponseDto(RouteDtoAssembler.toRouteEdgeResponseDto(pathResponseDto.getStations()),
                pathResponseDto.getDistance(), fare);
    }
}
