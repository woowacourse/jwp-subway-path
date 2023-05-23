package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.dto.PathResponse;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(final Long departureId, final Long arrivalId) {
        final List<Line> lines = lineRepository.findLines();
        final PathFinder pathFinder = PathFinder.generate(lines);

        final Station departure = stationService.findStationById(departureId);
        final Station arrival = stationService.findStationById(arrivalId);

        final Path path = pathFinder.findPath(departure, arrival);
        return PathResponse.of(path);
    }
}
