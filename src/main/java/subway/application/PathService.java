package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.graph.SubwayGraph;
import subway.dto.response.PathResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PathService {
    private final StationService stationService;
    private final FareCalculator fareCalculator;
    private final SubwayGraph subwayGraph;

    public PathService(final StationService stationService, final FareCalculator fareCalculator, final SubwayGraph subwayGraph) {
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
        this.subwayGraph = subwayGraph;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(final Long source, final Long target) {
        final Station sourceStation = stationService.findById(source);
        final Station targetStation = stationService.findById(target);

        final List<Station> shortestPaths = subwayGraph.findPath(sourceStation, targetStation);
        final int distanceSum = subwayGraph.calculateDistanceSum(sourceStation, targetStation);

        final int fareByDistance = fareCalculator.calculate(distanceSum);

        return new PathResponse(mapToString(shortestPaths), distanceSum, fareByDistance);
    }

    private List<String> mapToString(final List<Station> shortestPaths) {
        return shortestPaths.stream().map(Station::getName).collect(Collectors.toList());
    }

    public void update(final Lines lines) {
        subwayGraph.update(lines);
    }
}
