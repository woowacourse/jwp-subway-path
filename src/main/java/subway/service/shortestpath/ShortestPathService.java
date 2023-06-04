package subway.service.shortestpath;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.service.FareCalculateService;
import subway.domain.line.LineRepository;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.shortestpath.ShortestPath;
import subway.domain.shortestpath.ShortestPathFinder;
import subway.domain.station.StationRepository;
import subway.domain.subway.Subway;
import subway.dto.shortestpath.ShortestPathRequest;
import subway.dto.shortestpath.ShortestPathResponse;
import subway.dto.station.StationResponse;

@Transactional
@Service
public class ShortestPathService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final FareCalculateService fareCalculateService;

    public ShortestPathService(final LineRepository lineRepository,
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

        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(new Subway(subway));

        final ShortestPath shortestPath = shortestPathFinder.find(
                stationRepository.findByName(request.getStartStation()),
                stationRepository.findByName(request.getEndStation())
        );

        final List<StationResponse> stations = shortestPath.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        final int fare = fareCalculateService.calculate(shortestPath, request.getAge());

        return new ShortestPathResponse(stations, fare);
    }
}
