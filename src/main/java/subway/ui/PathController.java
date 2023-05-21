package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.ShortestPathResponse;
import subway.exception.NotValidException;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/path")
    public ResponseEntity<ShortestPathResponse> findShortestPath(
            @RequestParam(required = false) String startStationName,
            @RequestParam(required = false) String endStationName
    ) {
        if (startStationName == null || endStationName == null) {
            throw new NotValidException("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.");
        }
        ShortestPathResponse shortestPath = pathService.findShortestPath(startStationName, endStationName);
        return ResponseEntity.ok(shortestPath);
    }
}
