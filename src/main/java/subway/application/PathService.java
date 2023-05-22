package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.path.ShortestPathDto;
import subway.domain.Fare;
import subway.domain.FareCalculator;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public ShortestPathDto findPath(long sourceStationId, long destStationId) {
        PathFinder pathFinder = PathFinder.from(sectionRepository.findAll());
        Station source = stationRepository.findById(sourceStationId);
        Station dest = stationRepository.findById(destStationId);

        Path path = pathFinder.findShortestPath(source, dest);
        Fare fare = FareCalculator.calculate(path);
        return new ShortestPathDto(path, fare);
    }

}
