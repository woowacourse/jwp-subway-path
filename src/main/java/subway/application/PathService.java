package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.costpolicy.CostPolicyChain;
import subway.controller.dto.response.FindShortestPathResponse;
import subway.domain.Lines;
import subway.domain.Navigation;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.vo.Age;
import subway.exception.BusinessException;
import subway.persistence.LineRepository;
import subway.persistence.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private static final long DEFAULT_COST = 1250L;

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final CostPolicyChain costPolicyChain;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository,
        final CostPolicyChain costPolicyChain) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.costPolicyChain = costPolicyChain;
    }

    public FindShortestPathResponse findShortestPath(final String startStationName, final String endStationName,
        final int age) {
        final Station startStation = findStationByName(startStationName);
        final Station endStation = findStationByName(endStationName);
        final Lines lines = new Lines(lineRepository.findAll());
        final Navigation navigation = new Navigation(lines);
        final Path shortestPath = navigation.findShortestPath(startStation, endStation);
        final long totalCost = costPolicyChain.calculate(shortestPath, Age.from(age), DEFAULT_COST);
        return FindShortestPathResponse.of(shortestPath, totalCost);
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
            .orElseThrow(() -> new BusinessException("존재하지 않는 역입니다."));
    }
}
