package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Fare;
import subway.domain.Station;
import subway.dto.api.ShortestPathResponse;
import subway.dto.service.PathResult;
import subway.service.FareService;
import subway.service.PathService;
import subway.service.StationService;

@RestController
@RequestMapping("/path")
public class PathController {
    private final StationService stationService;
    private final PathService pathService;
    private final FareService fareService;

    public PathController(StationService stationService, PathService pathService, FareService fareService) {
        this.stationService = stationService;
        this.pathService = pathService;
        this.fareService = fareService;
    }

    @GetMapping
    public ResponseEntity<ShortestPathResponse> getPath(@RequestParam Long departureStationId,
                                                        @RequestParam Long arrivalStationId) {
        Station departure = stationService.findById(departureStationId);
        Station arrival = stationService.findById(arrivalStationId);
        PathResult pathResult = pathService.getShortestPath(departure, arrival);
        Fare fare = fareService.calculateFareOf(pathResult.getPath());

        ShortestPathResponse shortestPathResponse = ShortestPathResponse.of(departure, arrival, pathResult, fare);
        return ResponseEntity.ok().body(shortestPathResponse);
    }
}
