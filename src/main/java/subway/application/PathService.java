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
    private final LineService lineService;
    private final StationService stationService;
    private final FareCalculator fareCalculator;

    public PathService(final LineService lineService, final StationService stationService, final FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(final Long source, final Long target) {
        final Lines lines = lineService.findAll();
        final Station sourceStation = stationService.findById(source);
        final Station targetStation = stationService.findById(target);

        final SubwayGraph subwayGraph = SubwayGraph.from(lines);
        final List<Station> shortestPaths = subwayGraph.findPath(sourceStation, targetStation);
        final int distanceSum = subwayGraph.calculateDistanceSum(sourceStation, targetStation);

        final int fareByDistance = fareCalculator.calculate(distanceSum);

        return new PathResponse(mapToString(shortestPaths), distanceSum, fareByDistance);
    }

    private List<String> mapToString(final List<Station> shortestPaths) {
        return shortestPaths.stream().map(Station::getName).collect(Collectors.toList());
    }
}
