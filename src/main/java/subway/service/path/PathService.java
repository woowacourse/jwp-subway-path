package subway.service.path;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.service.FareCalculateService;
import subway.domain.line.LineRepository;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.StationRepository;
import subway.domain.subway.Subway;
import subway.dto.shortestpath.ShortestPathRequest;
import subway.dto.shortestpath.ShortestPathResponse;
import subway.dto.station.StationResponse;

@Transactional
@Service
public class PathService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final FareCalculateService fareCalculateService;

    public PathService(final LineRepository lineRepository,
                       final SectionRepository sectionRepository,
                       final StationRepository stationRepository,
                       final FareCalculateService fareCalculateService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.fareCalculateService = fareCalculateService;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final List<Sections> subway = lineRepository.findAll().stream()
                .map(line -> sectionRepository.findByLineId(line.getId()))
                .collect(Collectors.toList());

        final PathFinder pathFinder = new PathFinder(new Subway(subway));

        final Path path = pathFinder.findShortestPath(
                stationRepository.findByName(request.getStartStation()),
                stationRepository.findByName(request.getEndStation())
        );

        final List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        final int fare = fareCalculateService.calculate(path, request.getAge());

        return new ShortestPathResponse(stations, fare);
    }
}
