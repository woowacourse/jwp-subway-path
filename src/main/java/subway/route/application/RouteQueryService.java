package subway.route.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.route.domain.EdgeFinder;
import subway.route.domain.FareCalculator;
import subway.route.domain.InterStationEdge;
import subway.route.domain.PathCalculator;
import subway.route.dto.request.PathRequest;
import subway.route.dto.request.RouteFindRequest;
import subway.route.dto.response.PathResponse;
import subway.route.dto.response.RouteFindResponse;

@Service
public class RouteQueryService {

    private final EdgeFinder edgeFinder;
    private final PathCalculator pathCalculator;

    public RouteQueryService(EdgeFinder edgeFinder, PathCalculator pathCalculator) {
        this.edgeFinder = edgeFinder;
        this.pathCalculator = pathCalculator;
    }

    public RouteFindResponse findRoute(RouteFindRequest requestDto) {
        List<InterStationEdge> allEdges = edgeFinder.findAllEdges();
        PathResponse pathResponse = pathCalculator.calculatePath(
                new PathRequest(requestDto.getSourceId(), requestDto.getTargetId(), allEdges));
        FareCalculator fareCalculator = new FareCalculator();
        long fare = fareCalculator.calculateFare(pathResponse.getDistance());
        return new RouteFindResponse(pathResponse.getStations(), pathResponse.getDistance(), fare);
    }
}
