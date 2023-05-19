package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.controller.dto.RouteSearchResponse;

@RequestMapping("/subway")
@RestController
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/shortest-route")
    public ResponseEntity<RouteSearchResponse> findShortestRoute(@RequestParam String startStation,
                                                                 @RequestParam String endStation) {
        RouteSearchResponse routeResponse = subwayService.findRoute(startStation, endStation);
        return ResponseEntity.ok(routeResponse);
    }
}
