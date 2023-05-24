package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathResponse;
import subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") Long sourceId,
                                                         @RequestParam("target") Long targetId,
                                                         @RequestParam(value = "age",
                                                                 required = false,
                                                                 defaultValue = "19") int age) {
        final PathResponse response = pathService.findShortestPath(sourceId, targetId, age);
        return ResponseEntity.ok().body(response);
    }
}
