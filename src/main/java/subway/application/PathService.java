package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.fare.Fare;
import subway.domain.fare.FareStrategy;
import subway.domain.line.Line;
import subway.domain.path.JgraphPathFinder;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final FareStrategy fareStrategy;

    public PathService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final FareStrategy fareStrategy
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.fareStrategy = fareStrategy;
    }

    public PathResponse findPath(final PathRequest request) {
        final List<Line> lines = lineRepository.findAllLine();
        final Station departure = stationRepository.findById(request.getDepartureStationId());
        final Station arrival = stationRepository.findById(request.getArriveStationId());

        final PathFinder pathFinder = new JgraphPathFinder(lines);
        final Path path = pathFinder.findPath(departure, arrival);

        final Fare totalFare = fareStrategy.calculate(path.getPathDistance());

        return new PathResponse(
                createAllStationResponse(path.getPathStations()),
                totalFare.getFare(),
                path.getPathDistance().distance()
        );
    }

    private List<StationResponse> createAllStationResponse(final Stations stations) {
        return stations.stations().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private StationResponse createStationResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName().name());
    }
}
