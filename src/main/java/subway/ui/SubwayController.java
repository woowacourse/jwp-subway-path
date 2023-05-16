package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.application.SubwayService;
import subway.dto.StationEnrollRequest;
import subway.dto.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subway")
public class SubwayController {

    private final SubwayService subwayService;

    public SubwayController(final SubwayService subwayService) {
        this.subwayService = subwayService;
    }

    @PostMapping("/lines/{lineId}")
    public ResponseEntity<Void> enrollStation(@PathVariable final Long lineId,
                                              @RequestBody final StationEnrollRequest request) {
        subwayService.enrollStation(lineId, request);
        return ResponseEntity.created(URI.create("/lines/" + lineId)).build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable final Long lineId, @PathVariable final Long stationId) {
        subwayService.deleteStation(lineId, stationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header("Location", "/line/" + lineId).build();
    }

    @GetMapping("/lines/{lineId}")
    public ResponseEntity<List<StationResponse>> getRouteMap(@PathVariable final Long lineId) {
        return ResponseEntity.ok(subwayService.findRouteMap(lineId));
    }

    @GetMapping
    public ResponseEntity<Map<String, List<StationResponse>>> getAllRouteMap() {
        return ResponseEntity.ok(subwayService.findAllRouteMap());
    }
}
