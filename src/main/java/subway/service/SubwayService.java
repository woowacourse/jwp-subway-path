package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.SubwayShortestPathResponse;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.domain.subway.route_map.DijkstraRouteMap;
import subway.domain.subway.Subway;
import subway.domain.subway.billing_policy.BillingPolicyByDistance;
import subway.domain.subway.billing_policy.Fare;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class SubwayService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SubwayService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SubwayShortestPathResponse findShortestPath(final Long sourceStationId, final Long destinationStationId) {
        final List<Line> lines = lineRepository.findAll();
        final Station sourceStation = stationRepository.findById(sourceStationId);
        final Station destinationStation = stationRepository.findById(destinationStationId);

        final Subway subway = new Subway(new DijkstraRouteMap(lines), new BillingPolicyByDistance());

        final Path shortestPath = subway.findShortestPath(sourceStation, destinationStation);
        final Fare fare = subway.calculateFare(shortestPath);

        return SubwayShortestPathResponse.of(shortestPath, fare);
    }
}
