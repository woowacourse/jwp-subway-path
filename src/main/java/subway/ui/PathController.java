package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.ShortestPathRequest;
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
            @ModelAttribute final ShortestPathRequest request
    ) {
        if (request.getStartStationName() == null || request.getEndStationName() == null) {
            throw new NotValidException("최단 경로를 조회할 출발역과 도착역 모두 입력해주세요.");
        }
        ShortestPathResponse shortestPath = pathService.findShortestPath(request);
        return ResponseEntity.ok(shortestPath);
    }
}
