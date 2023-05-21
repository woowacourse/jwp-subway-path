package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.route.PathRequest;
import subway.dto.route.PathsResponse;
import subway.service.SubwayMapService;

import javax.validation.Valid;

@RequestMapping("/routes")
@RestController
public class RouteController {

    private final SubwayMapService subwayMapService;

    public RouteController(final SubwayMapService subwayMapService) {
        this.subwayMapService = subwayMapService;
    }

    @GetMapping
    public ResponseEntity<PathsResponse> findShortestPath(@RequestBody @Valid final PathRequest pathRequest) {
        return ResponseEntity.ok(subwayMapService.findShortestPath(pathRequest));
    }
}
