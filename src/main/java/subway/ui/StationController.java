package subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest request) {
        final Long savedSectionId = stationService.saveStation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/stations/" + savedSectionId))
                .build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStationByName(@PathVariable Long stationId) {
        stationService.deleteStation(stationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
