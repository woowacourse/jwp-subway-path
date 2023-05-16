package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.FindShortestPathRequest;
import subway.controller.dto.FindShortestPathResponse;
import subway.domain.CostPolicy;
import subway.domain.Lines;
import subway.domain.Navigation;
import subway.domain.Path;
import subway.domain.Station;
import subway.exception.BusinessException;
import subway.persistence.LineRepository;
import subway.persistence.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private static final String NOT_EXIST_STATION_MESSAGE = "존재하지 않는 역입니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final CostPolicy costPolicy;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
        final CostPolicy costPolicy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.costPolicy = costPolicy;
    }

    public FindShortestPathResponse findShortestPath(final FindShortestPathRequest request) {
        final Station startStation = getStation(request.getStartStationName());
        final Station endStation = getStation(request.getEndStationName());
        final Lines lines = new Lines(lineRepository.findAll());
        final Navigation navigation = new Navigation(lines);
        final Path shortestPath = navigation.findShortestPath(startStation, endStation);
        final long totalCost = costPolicy.calculate(shortestPath);
        return FindShortestPathResponse.of(shortestPath, totalCost);
    }

    private Station getStation(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException(NOT_EXIST_STATION_MESSAGE));
    }
}
