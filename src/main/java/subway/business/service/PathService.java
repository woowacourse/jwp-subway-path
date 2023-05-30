package subway.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.*;
import subway.business.domain.fare.DistanceFareStrategy;
import subway.business.service.dto.ShortestPathResponse;
import subway.business.service.path.PathCalculatorImpl;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PathService {
    private final DistanceFareStrategy distanceFareStrategy;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(DistanceFareStrategy distanceFareStrategy, LineRepository lineRepository, StationRepository stationRepository) {
        this.distanceFareStrategy = distanceFareStrategy;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public ShortestPathResponse getShortestPath(Long sourceStationId, Long destStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId);
        Station destStation = stationRepository.findById(destStationId);
        Subway subway = new Subway(lineRepository.findAll());
        PathCalculator pathCalculator = PathCalculatorImpl.from(subway);
        List<String> stationNamesOfShortestPath = pathCalculator.getShortestPath(sourceStation, destStation).stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        int totalDistance = pathCalculator.getTotalDistance(sourceStation, destStation);
        int totalFare = distanceFareStrategy.calculateFare(totalDistance);
        return new ShortestPathResponse(stationNamesOfShortestPath, totalDistance, totalFare);
    }
}
