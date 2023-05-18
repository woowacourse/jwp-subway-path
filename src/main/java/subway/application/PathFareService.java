package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.general.Money;
import subway.dto.PathDto;
import subway.dto.response.PathAndFareResponse;

@Service
public class PathFareService {

    private final PathService pathService;
    private final FarePolicy farePolicy;

    public PathFareService(PathService pathService, FarePolicy farePolicy) {
        this.pathService = pathService;
        this.farePolicy = farePolicy;
    }

    public PathAndFareResponse calculateRouteFare(Long startId, Long endId) {
        PathDto shortestPath = pathService.findShortest(startId, endId);
        Money fare = farePolicy.getFareFrom(shortestPath.getDistance());
        return new PathAndFareResponse(shortestPath.getPath(), fare.getMoney());
    }
}
