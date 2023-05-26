package subway.application.path.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.path.usecase.FindShortestPathUseCase;
import subway.domain.fare.Fare;
import subway.domain.fare.distanceproportion.TotalDistanceFareCalculator;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.path.SubwayPath;
import subway.domain.path.SubwayPathFinder;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.exception.NoDataFoundException;
import subway.ui.dto.response.ShortestPathResponse;
import subway.ui.dto.response.StationResponse;

@Transactional(readOnly = true)
@Service
public class FindShortestPathService implements FindShortestPathUseCase {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SubwayPathFinder subwayPathFinder;

    public FindShortestPathService(
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final SubwayPathFinder subwayPathFinder
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.subwayPathFinder = subwayPathFinder;
    }

    public ShortestPathResponse findShortestPath(final Long startStationId, final Long arrivalStationId) {
        final Station startStation = stationRepository.findById(startStationId)
                .orElseThrow(NoDataFoundException::new);
        final Station endStation = stationRepository.findById(arrivalStationId)
                .orElseThrow(NoDataFoundException::new);
        final List<Line> allLines = lineRepository.findAll();

        final SubwayPath shortestPath = subwayPathFinder.findShortestPath(allLines, startStation, endStation);
        final int pathDistance = shortestPath.getDistance();

        final TotalDistanceFareCalculator totalDistanceFareCalculator = new TotalDistanceFareCalculator();
        return toPathResponse(shortestPath, totalDistanceFareCalculator.calculateFareByDistance(pathDistance));
    }

    private ShortestPathResponse toPathResponse(final SubwayPath subwayPath, final Fare fare) {
        final List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());

        return new ShortestPathResponse(
                stationResponses,
                subwayPath.getDistance(),
                fare.getFare()
        );
    }
}
