package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.response.QueryShortestRouteResponse;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.route.RouteService;
import subway.domain.route.Route;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class RouteShortestService {

    private final RouteService routeService;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public RouteShortestService(
            final RouteService routeService,
            final LineRepository lineRepository,
            final StationRepository stationRepository
    ) {
        this.routeService = routeService;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public QueryShortestRouteResponse findByStartAndEnd(final String startStationName, final String endStationName) {
        final Station startStation = stationRepository.findByStationName(startStationName)
                .orElseThrow(() -> new IllegalArgumentException("이름으로 조회된 출발역이 존재하지 않습니다."));
        final Station endStation = stationRepository.findByStationName(endStationName)
                .orElseThrow(() -> new IllegalArgumentException("이름으로 조회된 도착역이 존재하지 않습니다."));
        final List<Line> lines = lineRepository.findAll();

        final Route findRoute = routeService.findRouteBy(lines, startStation, endStation);
        return QueryShortestRouteResponse.from(findRoute);
    }
}
