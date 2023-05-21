package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Fare;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.PathResponse;
import subway.persistence.repository.PathRepository;

@Service
public class PathService {
    private final PathRepository pathRepository;
    private final FareCalculator fareCalculator;

    public PathService(PathRepository pathRepository, FareCalculator fareCalculator) {
        this.pathRepository = pathRepository;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse getShortestPath(Long departureStationId, Long arrivalStationId) {
        Station departure = pathRepository.findStationById(departureStationId);
        Station arrival = pathRepository.findStationById(arrivalStationId);

        Sections sections = pathRepository.findAll();
        Subway subway = Subway.from(sections);

        SubwayGraph subwayGraph = SubwayGraph.from(subway);
        ShortestPath shortestPath = subwayGraph.getDijkstraShortestPath(departure, arrival);
        Fare fare = fareCalculator.calculate(shortestPath);

        return PathResponse.of(shortestPath, fare);
    }
}
