package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.path.Path;
import subway.domain.path.JgraphtPathFinder;
import subway.domain.Subway;
import subway.domain.fare.FarePolicy;
import subway.domain.path.PathFinder;
import subway.dto.ShortestPathResponse;
import subway.repository.LineRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final FarePolicy farePolicy;

    public PathService(final LineRepository lineRepository, final FarePolicy farePolicy) {
        this.lineRepository = lineRepository;
        this.farePolicy = farePolicy;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse findShortestPath(final String startStationName, final String endStationName) {
        Subway subway = new Subway(lineRepository.findAll());
        PathFinder pathFinder = new JgraphtPathFinder(subway);
        Path shortestPath = pathFinder.find(startStationName, endStationName);
        int fare = farePolicy.calculate(shortestPath.getDistance());

        return ShortestPathResponse.of(shortestPath, fare);
    }
}
