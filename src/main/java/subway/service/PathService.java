package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.fee.FeeInformation;
import subway.domain.fee.FeePolicy;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.dto.request.ShortestPathRequest;
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

    public PathService(
            final StationRepository stationRepository,
            final LineRepository lineRepository,
            final PathFinder pathFinder,
            final FeePolicy feePolicy
    ) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.feePolicy = feePolicy;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final Station startStation = stationRepository.findByName(request.getStartStation())
                .orElseThrow(() -> new NotFoundStationException(request.getStartStation()));
        final Station endStation = stationRepository.findByName(request.getEndStation())
                .orElseThrow(() -> new NotFoundStationException(request.getEndStation()));
        final Lines lines = lineRepository.findAll();
        final Path path = pathFinder.findShortestPath(startStation, endStation, lines);
        final int fee = feePolicy.calculate(new FeeInformation(path.getTotalDistance()));

        return ShortestPathResponse.of(path, fee);
    }
}
