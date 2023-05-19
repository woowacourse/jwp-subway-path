package subway.service;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.PathSegment;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.dto.service.PathResult;
import subway.exception.ArrivalSameWithDepartureException;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class PathService {
    private final PathFinderService pathFinderService;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(PathFinderService pathFinderService, LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.pathFinderService = pathFinderService;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResult getShortestPath(Station departure, Station arrival) {
        if (departure.equals(arrival)) {
            throw new ArrivalSameWithDepartureException();
        }
        Path shortestPath = pathFinderService.findPath(departure, arrival);

        return getPathResult(departure, arrival, shortestPath);
    }

    private Line findByPathSegment(PathSegment pathSegment) {
        return lineRepository.findById(pathSegment.getLineId())
                .orElseThrow(LineNotFoundException::new);
    }

    private List<Station> extractStations(PathSegment pathSegment) {
        List<Long> stationIds = pathSegment.getStationEdges().stream()
                .map(StationEdge::getDownStationId)
                .collect(Collectors.toList());
        return stationRepository.findById(stationIds);
    }

    private PathResult getPathResult(Station departure, Station arrival, Path shortestPath) {
        Map<Line, List<Station>> lineToStations = shortestPath.getPathSegments().stream()
                .collect(toMap(this::findByPathSegment, this::extractStations));

        return new PathResult(departure, arrival, lineToStations, shortestPath);
    }
}
