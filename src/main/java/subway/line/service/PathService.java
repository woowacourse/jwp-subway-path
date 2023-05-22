package subway.line.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.ShortestPathFinder;
import subway.line.dto.ShortestPathRequest;
import subway.line.dto.ShortestPathResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final ShortestPathFinder shortestPathFinder;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
        final ShortestPathFinder shortestPathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.shortestPathFinder = shortestPathFinder;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest shortestPathRequest) {
        final Station fromStation = stationRepository.findById(shortestPathRequest.getFromStationId());
        final Station toStation = stationRepository.findById(shortestPathRequest.getToStationId());
        final List<Line> lines = lineRepository.findAll();

        return shortestPathFinder.getShortestPathResponse(lines, fromStation,
            toStation);
    }
}
