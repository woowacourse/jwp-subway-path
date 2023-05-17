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

    public PathService(LineService lineService, StationService stationService, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Lines lines = lineService.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        SubwayGraph subwayGraph = SubwayGraph.from(lines);
        List<Station> shortestPaths = subwayGraph.findPath(sourceStation, targetStation);
        int distanceSum = subwayGraph.calculateDistanceSum(sourceStation, targetStation);

        int fareByDistance = fareCalculator.calculate(distanceSum);

        return new PathResponse(mapToString(shortestPaths), distanceSum, fareByDistance);
    }

    private List<String> mapToString(List<Station> shortestPaths) {
        return shortestPaths.stream().map(Station::getName).collect(Collectors.toList());
    }
}
