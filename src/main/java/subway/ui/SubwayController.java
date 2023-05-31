package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/subway")
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> findPath(@RequestBody PathRequest request) {
        PathResponse response = subwayService.findShortestPath(request);
        return ResponseEntity.ok(response);
    }
}
