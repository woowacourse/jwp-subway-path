package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.response.FindShortestPathResponse;
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

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final CostPolicy costPolicy;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
        final CostPolicy costPolicy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.costPolicy = costPolicy;
    }

    public FindShortestPathResponse findShortestPath(final String startStationName, final String endStationName) {
        final Station startStation = findStationByName(startStationName);
        final Station endStation = findStationByName(endStationName);
        final Lines lines = new Lines(lineRepository.findAll());
        final Navigation navigation = new Navigation(lines);
        final Path shortestPath = navigation.findShortestPath(startStation, endStation);
        final long totalCost = costPolicy.calculate(shortestPath);
        return FindShortestPathResponse.of(shortestPath, totalCost);
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다."));
    }
}
