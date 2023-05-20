package subway.path.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.path.facade.PathFacade;
import subway.path.presentation.dto.response.PathResponse;

@RequestMapping("/path")
@RestController
public class PathController {

    private final PathFacade pathFacade;

    public PathController(final PathFacade pathFacade) {
        this.pathFacade = pathFacade;
    }

    @GetMapping("/{upStationId}/{downStationId}")
    public ResponseEntity<PathResponse> findByDijkstra(
            @PathVariable Long upStationId,
            @PathVariable Long downStationId
    ) {
        PathResponse response = pathFacade.getByDijkstra(upStationId, downStationId);
        return ResponseEntity.ok().body(response);
    }

}
