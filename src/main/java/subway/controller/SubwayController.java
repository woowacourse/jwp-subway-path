package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.controller.dto.SubwayShortestPathResponse;
import subway.service.SubwayService;

@RestController
@RequestMapping("/subways")
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<SubwayShortestPathResponse> findShortestPath(
            @RequestParam Long sourceStationId,
            @RequestParam Long destinationStationId,
            @RequestParam Integer passengerAge
    ) {
        final SubwayShortestPathResponse response = subwayService.findShortestPath(
                sourceStationId,
                destinationStationId,
                passengerAge
        );
        return ResponseEntity.ok(response);
    }
}
