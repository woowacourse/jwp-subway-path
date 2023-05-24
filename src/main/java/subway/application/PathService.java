package subway.application;

import org.springframework.stereotype.Service;
import subway.application.fare.FareCalculator;
import subway.application.fare.FareCondition;
import subway.application.path.PathFinder;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.dto.ShortestPathResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathFinder pathFinder;
    private final List<FareCalculator> fareCalculator;

    public PathService(
            StationRepository stationRepository,
            SectionRepository sectionRepository,
            PathFinder pathFinder,
            List<FareCalculator> fareCalculator) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public ShortestPathResponse findShortestPath(Long upStationId, Long downStationId) {
        final Station upStation = stationRepository.findById(upStationId);
        final Station downStation = stationRepository.findById(downStationId);
        final MultiLineSections sections = sectionRepository.findAll();

        final ShortestPath shortestpath = pathFinder.findShortestPath(sections, upStation, downStation);
        Fare fare = calculateFare(shortestpath);

        return ShortestPathResponse.from(shortestpath, fare);
    }

    private Fare calculateFare(ShortestPath shortestpath) {
        Fare fare = Fare.zero();
        for (FareCalculator calculator : fareCalculator) {
            fare = calculator.calculateFare(FareCondition.from(shortestpath.getDistance()));
        }

        return fare;
    }
}
