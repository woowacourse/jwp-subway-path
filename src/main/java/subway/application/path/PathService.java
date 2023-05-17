package subway.application.path;

import org.springframework.stereotype.Service;
import subway.application.fare.FareCalculator;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.dto.ShortestPathResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(
            StationRepository stationRepository,
            SectionRepository sectionRepository,
            PathFinder pathFinder,
            FareCalculator fareCalculator) {
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
        final Fare fare = fareCalculator.calculateFare(shortestpath.getDistance());

        return ShortestPathResponse.from(shortestpath, fare);
    }
}
