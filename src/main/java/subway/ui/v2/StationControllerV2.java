package subway.ui.v2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.request.CreateStationRequest;
import subway.application.v2.StationServiceV2;
import subway.dto.StationResponse;

import java.net.URI;

@RequestMapping("/v2/stations")
@RestController
public class StationControllerV2 {

    private final StationServiceV2 stationServiceV2;

    public StationControllerV2(final StationServiceV2 stationServiceV2) {
        this.stationServiceV2 = stationServiceV2;
    }

    @PostMapping
    public ResponseEntity<Long> createStation(@RequestBody CreateStationRequest request) {
        final Long saveStationId = stationServiceV2.saveStation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/stations/" + saveStationId))
                .body(saveStationId);
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationResponse> findStation(@PathVariable Long stationId) {
        final StationResponse response = stationServiceV2.findByStationId(stationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
