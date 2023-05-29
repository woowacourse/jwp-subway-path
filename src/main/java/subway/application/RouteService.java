package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Station;
import subway.domain.Subway;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.PassengerAge;
import subway.domain.routestrategy.RouteStrategy;
import subway.domain.routestrategy.SubwaySection;
import subway.dto.RouteRequest;
import subway.repository.LineRepository;

@Service
public class RouteService {

    private final RouteStrategy routeStrategy;
    private final FareCalculator fareCalculator;
    private final LineRepository lineRepository;
    
    public RouteService(RouteStrategy routeStrategy, FareCalculator fareCalculator, LineRepository lineRepository) {
        this.routeStrategy = routeStrategy;
        this.fareCalculator = fareCalculator;
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
        Subway subway = new Subway(lineRepository.findAll());
        List<SubwaySection> route = routeStrategy.findShortestSections(subway,
                subway.findStationByName(request.getStartStation()),
                subway.findStationByName(request.getEndStation())
        );
        PassengerAge age = PassengerAge.from(request.getAgeOfPassenger());
        return fareCalculator.calculateFare(age, route);
    }
}
