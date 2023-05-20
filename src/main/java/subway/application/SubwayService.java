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
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class SubwayService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FarePolicy farePolicy;

    public SubwayService(LineRepository lineRepository, StationRepository stationRepository, FarePolicy farePolicy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicy = farePolicy;
    }

    public RouteSearchResponse findRoute(String startStationName, String endStationName) {
        Station startStation = stationRepository.findByName(startStationName);
        Station endStation = stationRepository.findByName(endStationName);
        List<Line> lines = lineRepository.findAll();
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
