package subway.controller;

import javax.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.ShortestPathResponse;
import subway.service.PathService;

@Validated
@RestController
@RequestMapping("/path")
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/{startStationId}/{endStationId}")
    public ResponseEntity<ShortestPathResponse> findShortestPath(
            @PathVariable("startStationId") @Positive(message = "역 ID는 양수여야 합니다.") final Long startStationId,
            @PathVariable("endStationId") @Positive(message = "역 ID는 양수여야 합니다.") final Long endStationId
    ) {
        ShortestPathResponse shortestPathResponse = pathService.findShortestPath(startStationId, endStationId);
        return ResponseEntity.ok(shortestPathResponse);
    }
}
