package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.RouteSearchResponse;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.domain.route.JgraphtRouteGraph;
import subway.domain.route.RouteGraph;

@Transactional(readOnly = true)
@Service
public class SubwayService {

    private final LineService lineService;
    private final StationService stationService;

    public SubwayService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public RouteSearchResponse findRoute(String startStationName, String endStationName) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);
        List<Line> lines = lineService.findAllLines();
        RouteGraph routeGraph = JgraphtRouteGraph.from(lines);

        List<Station> routes = routeGraph.findShortestRoute(startStation, endStation);
        List<Line> linesOnRoutes = routeGraph.findLinesOnRoutes(startStation, endStation);
        Distance distance = routeGraph.findShortestDistance(startStation, endStation);

        Fare fare = calculateFare(linesOnRoutes, distance);
        return new RouteSearchResponse(routes, distance.getValue(), fare.getValue());
    }

    private Fare calculateFare(List<Line> linesOnRoute, Distance distance) {
        Fare fare = DistanceFarePolicy.calculateFare(distance);
        int extraFare = linesOnRoute.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("노선의 추가 요금을 찾을 수 없습니다."));
        return new Fare(fare.getValue() + extraFare);
    }
}
