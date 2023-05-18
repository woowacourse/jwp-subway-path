package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.application.FarePolicy;
import subway.application.PathService;
import subway.domain.general.Money;
import subway.dto.PathDto;
import subway.dto.response.PathAndFareResponse;

@RestController
public class PathController {

    private final PathService pathService;
    private final FarePolicy farePolicy;

    public PathController(PathService pathService, FarePolicy farePolicy) {
        this.pathService = pathService;
        this.farePolicy = farePolicy;
    }

    @GetMapping("/paths/{startId}/{endId}")
    public ResponseEntity<PathAndFareResponse> findStationsInLine(@PathVariable Long startId, @PathVariable Long endId) {
        PathDto shortestPath = pathService.findShortest(startId, endId);
        Money fare = farePolicy.getFareFrom(shortestPath.getDistance());
        PathAndFareResponse response = new PathAndFareResponse(shortestPath.getPath(), fare.getMoney());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
