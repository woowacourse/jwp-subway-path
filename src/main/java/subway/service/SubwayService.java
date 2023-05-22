package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.PassengerRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.domain.fare.FareStrategy;
import subway.domain.line.Line;
import subway.domain.section.PathSection;
import subway.domain.station.Station;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;
import subway.domain.subway.SubwayGraph;
import subway.domain.subway.SubwayJgraphtGraph;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class SubwayService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FareStrategy fareStrategy;

    public SubwayService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final FareStrategy fareStrategy
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.fareStrategy = fareStrategy;
    }

    public ShortestPathResponse findShortestPath(final PassengerRequest request) {
        final List<Line> lines = lineRepository.findAll();
        final Subway subway = generateSubway(lines);
        final Passenger passenger = generatePassenger(request);

        final List<PathSection> pathSections =
                subway.findShortestPathSections(passenger.getStart(), passenger.getEnd());
        final long totalDistance = subway.calculateShortestDistance(passenger.getStart(), passenger.getEnd());
        final long subwayFare = (long) fareStrategy.calculateFare(0, passenger, subway);

        return ShortestPathResponse.of(pathSections, totalDistance, subwayFare);
    }

    private Subway generateSubway(final List<Line> lines) {
        final SubwayGraph subwayGraph = new SubwayJgraphtGraph(lines);
        return new Subway(subwayGraph);
    }

    private Passenger generatePassenger(final PassengerRequest request) {
        final Station start = stationRepository.findById(request.getStartStationId());
        final Station end = stationRepository.findById(request.getEndStationId());
        return new Passenger(request.getAge(), start, end);
    }
}

