package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.FareCalculator;
import subway.domain.Sections;
import subway.domain.ShortestPath;
import subway.domain.ShortestPathFinder;
import subway.domain.Subway;
import subway.dto.shortestpath.ShortestPathRequest;
import subway.dto.shortestpath.ShortestPathResponse;
import subway.dto.station.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional
@Service
public class ShortestPathService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public ShortestPathService(final LineRepository lineRepository, final SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final List<Sections> subway = lineRepository.findAll().stream()
                .map(line -> sectionRepository.findByLineId(line.getId()))
                .collect(Collectors.toList());
        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(new Subway(subway));
        final ShortestPath shortestPath = shortestPathFinder.find(request.getStartStation(), request.getEndStation());
        final List<StationResponse> stations = shortestPath.getStations().stream()
                .map(station -> stationRepository.findByName(station.getName()))
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new ShortestPathResponse(stations, FareCalculator.calculate(shortestPath.getShortestDistance()));
    }
}
