package subway.domain.path.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import subway.domain.path.domain.LinePath;
import subway.domain.path.domain.ShortestPath;
import subway.domain.path.dto.PathResponse;
import subway.domain.path.service.PathService;
import subway.global.common.ResultResponse;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/path")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/lines")
    public ResponseEntity<ResultResponse> findAllLine() {
        List<LinePath> linePaths = pathService.findAll();
        List<PathResponse> pathResponse = linePaths.stream()
                .map(PathResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, pathResponse));
    }

    @GetMapping("/line/{id}")
    public ResponseEntity<ResultResponse> findLineById(@PathVariable final Long id) {
        LinePath linePath = pathService.findById(id);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, PathResponse.of(linePath)));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> findPath(@RequestParam final Long startLineId, @RequestParam final Long endLineId) {
        ShortestPath shortestPath = pathService.findShortestPath(startLineId, endLineId);
        return ResponseEntity.ok().body(ResultResponse.of(HttpStatus.OK, shortestPath));
    }
}
