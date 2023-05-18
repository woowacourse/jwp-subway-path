package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import subway.application.SubwayService;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/subway")
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<PathResponse> getShortestPath(
            @RequestParam("from") Long fromStationId,
            @RequestParam("to") Long toStationId
    ) {
        PathResponse response = subwayService.getShortestPath(fromStationId, toStationId);
        return ResponseEntity.ok(response);
    }
}
