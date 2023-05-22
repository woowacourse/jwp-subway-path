package subway.line.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.ShortestPathRequest;
import subway.line.dto.ShortestPathResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest shortestPathRequest) {
        final Station fromStation = stationRepository.findById(shortestPathRequest.getFromStationId());
        final Station toStation = stationRepository.findById(shortestPathRequest.getToStationId());
        final List<Line> lines = lineRepository.findAll();

        final ShortestPathFinder shortestPathFinder = new JgraphtShortestPathFinder();
        shortestPathFinder.makeGraph(lines);

        final ShortestPathResponse shortestPathResponse = shortestPathFinder.getShortestPathResponse(fromStation,
            toStation);
        return shortestPathResponse;
    }
}
