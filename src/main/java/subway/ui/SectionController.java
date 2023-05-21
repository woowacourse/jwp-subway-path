package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.dto.PathResponse;

@RestController
public class SectionController {

    private final PathService pathService;

    public SectionController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/sections/short-path")
    public ResponseEntity<PathResponse> getShortestPath(@RequestParam("startLine") String startLineName, @RequestParam("startStation") String startStationName,
                                @RequestParam("endLine") String endLineName, @RequestParam("endStation") String endStationName) {
        PathResponse pathResponse = pathService.findShortestPath(startLineName, startStationName, endLineName, endStationName);
        return ResponseEntity.ok(pathResponse);
    }
}
