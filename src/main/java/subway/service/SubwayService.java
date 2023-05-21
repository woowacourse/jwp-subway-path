package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.SubwayShortestPathResponse;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.passenger.Passenger;
import subway.domain.station.Station;
import subway.domain.subway.Subway;
import subway.domain.subway.billing_policy.Fare;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class SubwayService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final Subway subway;

    public SubwayService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final Subway subway
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.subway = subway;
    }

    public SubwayShortestPathResponse findShortestPath(
            final Long sourceStationId,
            final Long destinationStationId,
            final int passengerAge
    ) {
        final List<Line> lines = lineRepository.findAll();
        final Station sourceStation = stationRepository.findById(sourceStationId);
        final Station destinationStation = stationRepository.findById(destinationStationId);
        final Passenger passenger = new Passenger(passengerAge);

        subway.updateRouteMap(lines);

        final Path path = subway.findShortestPath(sourceStation, destinationStation);
        final Fare fare = subway.calculateFare(path, passenger);

        return SubwayShortestPathResponse.of(path, fare);
    }
}
