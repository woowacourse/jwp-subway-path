package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.FeePolicy;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Station;
import subway.dto.response.ShortestPathResponse;
import subway.exception.NotFoundStationException;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;
    private final FeePolicy feePolicy;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder,
                       FeePolicy feePolicy) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.feePolicy = feePolicy;
    }

    public ShortestPathResponse findShortestPath(String startStationName,
                                                 String endStationName) {
        Station startStation = stationRepository.findByName(startStationName)
                .orElseThrow(() -> new NotFoundStationException(startStationName));
        Station endStation = stationRepository.findByName(endStationName)
                .orElseThrow(() -> new NotFoundStationException(endStationName));
        List<Line> lines = lineRepository.findAll();
        Path path = pathFinder.find(startStation, endStation, lines);
        int fee = feePolicy.calculate(path.getTotalDistance());
        return ShortestPathResponse.of(path, fee);
    }
}
