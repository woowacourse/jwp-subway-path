package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.RouteSearchResponse;
import subway.controller.dto.StationResponse;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicy;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.domain.route.JgraphtRouteGraph;
import subway.domain.route.RouteGraph;
import subway.domain.route.Subway;

@Transactional(readOnly = true)
@Service
public class SubwayService {

    private final LineService lineService;
    private final StationService stationService;
    private final FarePolicy farePolicy;

    public SubwayService(LineService lineService, StationService stationService, FarePolicy farePolicy) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.farePolicy = farePolicy;
    }

    public RouteSearchResponse findRoute(String startStationName, String endStationName) {
        Station startStation = stationService.findByName(startStationName);
        Station endStation = stationService.findByName(endStationName);
        List<Line> lines = lineService.findAllLines();
        RouteGraph routeGraph = JgraphtRouteGraph.from(lines);

        Subway subway = new Subway(routeGraph);
        List<StationResponse> routes = findShortestRoute(subway, startStation, endStation);
        Distance distance = subway.findShortestDistance(startStation, endStation);
        Fare fare = farePolicy.calculate(distance);
        return new RouteSearchResponse(routes, distance.getValue(), fare.getValue());
    }

    private static List<StationResponse> findShortestRoute(Subway subway, Station startStation,
                                                           Station endStation) {
        return subway.findShortestRoute(startStation, endStation)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
