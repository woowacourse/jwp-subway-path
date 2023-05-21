package subway.domain.line.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.domain.line.domain.Path;
import subway.domain.line.dto.PathResponse;
import subway.domain.line.service.PathService;
import subway.global.common.ResultResponse;

@Controller
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findPath(@RequestParam final Long startLineId, @RequestParam final Long endLineId) {
        Path path = pathService.findShortestPath(startLineId, endLineId);

        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, PathResponse.from(path)));
    }
}
