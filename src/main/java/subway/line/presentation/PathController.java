package subway.line.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.line.dto.ShortestPathRequest;
import subway.line.dto.ShortestPathResponse;
import subway.line.service.PathService;

@RestController
@RequestMapping("/path")
public class PathController {

  private final PathService pathService;

  public PathController(PathService pathService) {
    this.pathService = pathService;
  }

  @GetMapping
  public ResponseEntity<ShortestPathResponse> getShortestPath(@RequestBody final ShortestPathRequest shortestPathRequest) {
    return ResponseEntity.ok(pathService.findShortestPath(shortestPathRequest));
  }
}
