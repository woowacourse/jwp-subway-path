package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateStationRequest;
import subway.application.StationService;
import subway.application.response.StationResponse;

import java.net.URI;

@RequestMapping("/stations")
@RestController
public class StationController {

    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Long> createStation(@RequestBody CreateStationRequest request) {
        final Long saveStationId = stationService.saveStation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/stations/" + saveStationId))
                .body(saveStationId);
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationResponse> findStation(@PathVariable Long stationId) {
        final StationResponse response = stationService.findByStationId(stationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
