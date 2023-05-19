package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.RouteSearchResponse;
import subway.controller.dto.StationResponse;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.FarePolicy;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Station;
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
        Route route = Route.from(lines);

        List<StationResponse> routes = findShortestRoute(route, startStation, endStation);
        Distance distance = route.findShortestDistance(startStation, endStation);
        Fare fare = farePolicy.calculate(distance);
        return new RouteSearchResponse(routes, distance.getValue(), fare.getValue());
    }

    private static List<StationResponse> findShortestRoute(Route route, Station startStation, Station endStation) {
        return route.findShortestRoute(startStation, endStation)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
