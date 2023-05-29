package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import subway.application.PathService;
import subway.dto.ShortPathRequest;
import subway.dto.ShortPathResponse;

@RequestMapping("/path")
@Controller
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ShortPathResponse> findShortPath(@RequestBody ShortPathRequest shortPathRequest) {
        final ShortPathResponse shortestPathResponse = pathService.findShortestPathInfo(
                shortPathRequest.getStartStationName(), shortPathRequest.getEndStationName());
        return ResponseEntity.ok(shortestPathResponse);
    }

}
