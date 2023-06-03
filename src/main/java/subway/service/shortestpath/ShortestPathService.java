package subway.service.shortestpath;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.fare.service.FareCalculateService;
import subway.domain.section.Sections;
import subway.domain.shortestpath.ShortestPath;
import subway.domain.shortestpath.ShortestPathFinder;
import subway.domain.subway.Subway;
import subway.dto.shortestpath.ShortestPathRequest;
import subway.dto.shortestpath.ShortestPathResponse;
import subway.dto.station.StationResponse;
import subway.persistence.repository.line.JdbcLineRepository;
import subway.persistence.repository.section.JdbcSectionRepository;
import subway.persistence.repository.station.JdbcStationRepository;

@Transactional
@Service
public class ShortestPathService {

    private final JdbcLineRepository jdbcLineRepository;
    private final JdbcSectionRepository jdbcSectionRepository;
    private final JdbcStationRepository jdbcStationRepository;
    private final FareCalculateService fareCalculateService;

    public ShortestPathService(final JdbcLineRepository jdbcLineRepository,
                               final JdbcSectionRepository jdbcSectionRepository,
                               final JdbcStationRepository jdbcStationRepository,
                               final FareCalculateService fareCalculateService) {
        this.jdbcLineRepository = jdbcLineRepository;
        this.jdbcSectionRepository = jdbcSectionRepository;
        this.jdbcStationRepository = jdbcStationRepository;
        this.fareCalculateService = fareCalculateService;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final List<Sections> subway = jdbcLineRepository.findAll().stream()
                .map(line -> jdbcSectionRepository.findByLineId(line.getId()))
                .collect(Collectors.toList());

        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(new Subway(subway));

        final ShortestPath shortestPath = shortestPathFinder.find(
                jdbcStationRepository.findByName(request.getStartStation()),
                jdbcStationRepository.findByName(request.getEndStation())
        );

        final List<StationResponse> stations = shortestPath.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        final int fare = fareCalculateService.calculate(shortestPath, request.getAge());

        return new ShortestPathResponse(stations, fare);
    }
}
