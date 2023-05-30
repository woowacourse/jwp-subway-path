package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.domain.Station;
import subway.domain.path.ShortestPath;
import subway.dto.ShortestPathResponse;
import subway.dto.SubwayPathRequest;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestController
public class PathController {

    private final SubwayService subwayService;

    public PathController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/paths")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody SubwayPathRequest subwayPathRequest) {
        ShortestPath shortestPath = subwayService.findShortestPath(subwayPathRequest);
        return ResponseEntity.ok(toShortestPathResponse(shortestPath, shortestPath.calculateFare()));
    }

    private ShortestPathResponse toShortestPathResponse(ShortestPath shortestPath, int fare) {
        return shortestPath.getStations()
                .stream()
                .map(Station::getName)
                .collect(collectingAndThen(
                        toList(),
                        stations -> new ShortestPathResponse(stations, shortestPath.getDistance(), fare))
                );
    }
}
