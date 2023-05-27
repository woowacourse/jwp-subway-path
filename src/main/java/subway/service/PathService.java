package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.fare.Fare;
import subway.domain.fare.FarePolicies;
import subway.domain.route.DijkstraRouteMap;
import subway.domain.route.Path;
import subway.domain.route.RouteMap;
import subway.dto.PathStationResponse;
import subway.dto.ShortestPathResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FarePolicies farePolicies;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
                       final FarePolicies farePolicies) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.farePolicies = farePolicies;
    }

    public ShortestPathResponse findShortestPath(final Long startStationId, final Long endStationId) {
        final Lines lines = lineRepository.findAll();
        final RouteMap routeMap = new DijkstraRouteMap(lines);

        final Station startStation = stationRepository.findById(startStationId);
        final Station endStation = stationRepository.findById(endStationId);

        final Path shortestPath = routeMap.findShortestPath(startStation, endStation);
        final Fare fare = farePolicies.calculateFare(shortestPath);

        return toResponse(shortestPath, fare);
    }

    private ShortestPathResponse toResponse(final Path path, final Fare fare) {
        List<PathStationResponse> pathStationResponses = path.getPath().stream()
                .map(station -> new PathStationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        int totalDistance = path.getDistance();

        return new ShortestPathResponse(pathStationResponses, totalDistance, fare.getValue());
    }
}
