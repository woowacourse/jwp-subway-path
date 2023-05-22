package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.ShortestPathResponse;
import subway.dto.SubwayPathRequest;

@RestController
public class PathController {

    private final SubwayService subwayService;

    public PathController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/paths")
    public ResponseEntity<ShortestPathResponse> findShortestPath(@RequestBody SubwayPathRequest subwayPathRequest) {
        return ResponseEntity.ok(subwayService.findShortestPath(subwayPathRequest));
    }
}
