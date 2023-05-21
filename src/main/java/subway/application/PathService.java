package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.path.PathFindDto;
import subway.application.dto.path.ShortestPathDto;
import subway.domain.FareCalculator;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.LineRepository;
import subway.domain.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final FareCalculator fareCalculator;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(FareCalculator fareCalculator, LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.fareCalculator = fareCalculator;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public ShortestPathDto findPath(PathFindDto pathFindDto) {
        PathFinder pathFinder = PathFinder.from(findAllSections());
        Station source = stationRepository.findById(pathFindDto.getSourceStationId());
        Station dest = stationRepository.findById(pathFindDto.getDestStationId());

        Path path = pathFinder.findShortestPath(source, dest);
        int fare = fareCalculator.calculate(path);
        return new ShortestPathDto(path, fare);
    }

    private List<Sections> findAllSections() {
        return lineRepository.findAll()
                .stream()
                .map(Line::getSections)
                .collect(Collectors.toList());
    }

}
