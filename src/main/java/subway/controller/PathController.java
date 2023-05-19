package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.domain.Subway;
import subway.dto.PathResponse;
import subway.service.PathService;
import subway.service.SubwayService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;
    private final SubwayService subwayService;

    public PathController(final PathService pathService, final SubwayService subwayService) {
        this.pathService = pathService;
        this.subwayService = subwayService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") Long sourceId,
                                                         @RequestParam("target") Long targetId) {

        final Subway subway = subwayService.findSubway();
        final PathResponse response = pathService.findShortestPath(sourceId, targetId);
        return ResponseEntity.ok().body(response);
    }
}
