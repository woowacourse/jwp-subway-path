package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.route.RouteShortCutRequest;
import subway.dto.route.RouteShortCutResponse;
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
    public ResponseEntity<RouteShortCutResponse> findShortCutRoute(@RequestBody @Valid final RouteShortCutRequest routeShortCutRequest) {
        return ResponseEntity.ok(subwayMapService.findShortCutRoute(routeShortCutRequest));
    }
}
