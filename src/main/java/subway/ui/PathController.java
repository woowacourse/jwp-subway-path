package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import subway.service.SubwayService;
import subway.service.dto.PathDto;
import subway.ui.dto.PathResponse;

@RestController
@RequestMapping("/path")
public class PathController {

    private final SubwayService subwayService;

    public PathController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
            @RequestParam("startstation") final Long startStationId,
            @RequestParam("destinationstation") final Long destinationStationId
    ) {
        final PathDto path = subwayService.findPath(startStationId, destinationStationId);
        return ResponseEntity.ok().body(PathResponse.from(path));
    }

}
