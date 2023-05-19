package subway.controller;

import javax.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.controller.dto.RouteSearchResponse;

@Validated
@RequestMapping("/subway")
@RestController
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/shortest-route")
    public ResponseEntity<RouteSearchResponse> findShortestRoute(
            @NotBlank(message = "출발역은 반드시 입력해야 합니다.") @RequestParam String startStation,
            @NotBlank(message = "도착역은 반드시 입력해야 합니다.") @RequestParam String endStation) {
        RouteSearchResponse routeResponse = subwayService.findRoute(startStation, endStation);
        return ResponseEntity.ok(routeResponse);
    }
}
