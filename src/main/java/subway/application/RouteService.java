package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Station;
import subway.domain.Subway;
import subway.domain.SubwayFareStrategy;
import subway.domain.routestrategy.RouteStrategy;
import subway.dto.RouteRequest;
import subway.repository.LineRepository;

@Service
public class RouteService {

    private final RouteStrategy routeStrategy;
    private final SubwayFareStrategy subwayFareStrategy;
    private final LineRepository lineRepository;

    public RouteService(RouteStrategy routeStrategy, SubwayFareStrategy subwayFareStrategy,
            LineRepository lineRepository) {
        this.routeStrategy = routeStrategy;
        this.subwayFareStrategy = subwayFareStrategy;
        this.lineRepository = lineRepository;
    }

    public List<Station> findShortestRoute(RouteRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        return routeStrategy.findShortestRoute(subway,
                subway.findStationByName(request.getStartStation()),
                subway.findStationByName(request.getEndStation())
                );
    }

    public Distance findShortestDistance(RouteRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        return routeStrategy.findShortestDistance(subway,
                subway.findStationByName(request.getStartStation()),
                subway.findStationByName(request.getEndStation())
        );
    }

    public Fare findShortestRouteFare(RouteRequest request) {
        Distance totalDistance = findShortestDistance(request);
        return subwayFareStrategy.calculteFare(totalDistance);
    }
}
