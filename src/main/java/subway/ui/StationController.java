package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.application.request.CreateSectionRequest;
import subway.application.request.DeleteStationRequest;
import subway.application.response.StationResponse;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/stations")
@RestController
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Long> createStation(@Valid @RequestBody CreateSectionRequest request) {
        final Long lineId = stationService.saveSection(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/lines/" + lineId))
                .body(lineId);
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationResponse> findStation(@PathVariable Long stationId) {
        final StationResponse response = stationService.findByStationId(stationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStation(@Valid @RequestBody DeleteStationRequest request) {
        stationService.deleteByStationNameAndLineName(request);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
