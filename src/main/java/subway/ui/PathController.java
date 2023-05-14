package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.service.PathService;
import subway.dto.request.AddPathRequest;

import javax.validation.Valid;

@RequestMapping("/lines/{line-id}/stations")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @PostMapping
    public ResponseEntity<Void> addStationInPath(
            @PathVariable("line-id") final Long lineId,
            @Valid @RequestBody final AddPathRequest addPathRequest
    ) {
        pathService.addPath(
                lineId,
                addPathRequest.getTargetStationId(),
                addPathRequest.getAddStationId(),
                addPathRequest.getDistance(),
                addPathRequest.getDirection()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{station-id}")
    public ResponseEntity<Void> deleteStationFromPath(
            @PathVariable("line-id") Long lineId,
            @PathVariable("station-id") Long stationId
    ) {
        pathService.removeStationFromLine(lineId, stationId);

        return ResponseEntity.ok().build();
    }

}
