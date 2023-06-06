package subway.route.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.route.domain.FareCalculator;
import subway.route.domain.InterStationEdge;
import subway.route.domain.PathCalculator;
import subway.route.domain.RouteAllEdgesUseCase;
import subway.route.dto.request.PathRequestDto;
import subway.route.dto.request.RouteFindRequestDto;
import subway.route.dto.response.PathResponseDto;
import subway.route.dto.response.RouteFindResponseDto;

@Service
public class RouteQueryService {

    private final RouteAllEdgesUseCase routeAllEdgesUseCase;
    private final PathCalculator pathCalculator;

    public RouteQueryService(RouteAllEdgesUseCase routeAllEdgesUseCase, PathCalculator pathCalculator) {
        this.routeAllEdgesUseCase = routeAllEdgesUseCase;
        this.pathCalculator = pathCalculator;
    }

    public RouteFindResponseDto findRoute(RouteFindRequestDto requestDto) {
        List<InterStationEdge> allEdges = routeAllEdgesUseCase.findAllEdges();
        PathResponseDto pathResponseDto = pathCalculator.calculatePath(
                new PathRequestDto(requestDto.getSource(), requestDto.getTarget(), allEdges));
        FareCalculator fareCalculator = new FareCalculator();
        long fare = fareCalculator.calculateFare(pathResponseDto.getDistance());
        return new RouteFindResponseDto(RouteDtoAssembler.toRouteEdgeResponseDto(pathResponseDto.getStations()),
                pathResponseDto.getDistance(), fare);
    }
}
