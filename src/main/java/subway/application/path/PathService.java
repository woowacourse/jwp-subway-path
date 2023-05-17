package subway.application.path;

import org.springframework.stereotype.Service;
import subway.application.fare.Fare;
import subway.application.fare.FareCalculator;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.ShortestPathResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

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
        final List<Section> sections = sectionRepository.findAll();

        final ShortestPath shortestpath = pathFinder.findShortestPath(sections, upStation, downStation);
        final Fare fare = fareCalculator.calculateFare(shortestpath.getDistance());

        return ShortestPathResponse.from(shortestpath, fare);
    }
}
