package subway.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
            @RequestParam @NotBlank(message = "출발역은 반드시 입력해야 합니다.") String startStation,
            @RequestParam @NotBlank(message = "도착역은 반드시 입력해야 합니다.") String endStation,
            @RequestParam @Positive(message = "나이는 1살 이상부터 가능합니다.") Integer age
    ) {
        RouteSearchResponse routeResponse = subwayService.findRoute(startStation, endStation, age);
        return ResponseEntity.ok(routeResponse);
    }
}
