package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.PathService;
import subway.application.dto.ShortestPathInfoDto;
import subway.presentation.dto.response.ShortestPathInfoResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/shortest/{sourceStationId}/{targetStationId}")
    public ResponseEntity<ShortestPathInfoResponse> findShortestPathAndFare(
            @PathVariable final Long sourceStationId,
            @PathVariable final Long targetStationId
    ) {
        final ShortestPathInfoDto dto = pathService.findShortestPathInfo(sourceStationId, targetStationId);

        return ResponseEntity.ok(ShortestPathInfoResponse.from(dto));
    }
}
