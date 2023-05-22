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
    public ResponseEntity<Void> createStation(@RequestBody StationRequest request) {
        final Long savedSectionId = stationService.saveStation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/stations/" + savedSectionId))
                .build();
    }

    @GetMapping("/{stationId}")
    public ResponseEntity<StationResponse> readStation(@PathVariable Long stationId) {
        StationResponse station = stationService.findStation(stationId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(station);
    }

    @PutMapping("/{stationId}")
    public ResponseEntity<Void> updateStation(@PathVariable Long stationId, @RequestBody StationRequest request) {
        stationService.editStation(stationId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/stations/" + stationId))
                .build();
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStationByName(@PathVariable Long stationId) {
        stationService.removeStation(stationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
