package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.path.PathFindDto;
import subway.application.dto.path.ShortestPathDto;
import subway.domain.FareCalculator;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final FareCalculator fareCalculator;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(FareCalculator fareCalculator,
                       StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.fareCalculator = fareCalculator;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public ShortestPathDto findPath(PathFindDto pathFindDto) {
        PathFinder pathFinder = PathFinder.from(sectionRepository.findAll());
        Station source = stationRepository.findById(pathFindDto.getSourceStationId());
        Station dest = stationRepository.findById(pathFindDto.getDestStationId());

        Path path = pathFinder.findShortestPath(source, dest);
        int fare = fareCalculator.calculate(path);
        return new ShortestPathDto(path, fare);
    }

}
